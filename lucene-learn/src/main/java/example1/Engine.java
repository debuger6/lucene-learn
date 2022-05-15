package example1;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.codecs.lucene80.Lucene80Codec;
import org.apache.lucene.codecs.simpletext.SimpleTextCodec;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.MMapDirectory;
import org.apache.lucene.store.SimpleFSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Engine {
    // 用于索引的基本数据结构
    private IndexWriter writer;
    private Analyzer analyzer;
    private Directory directory;

    public Engine(String dataPath) throws IOException {
        // 使用标准分词器
        this.analyzer = new StandardAnalyzer();
        this.directory = new MMapDirectory(Paths.get(dataPath));
        //this.directory = new SimpleFSDirectory(Paths.get(dataPath));

        // 注：用 SimpleTextCodec 可以读懂数据文件内容，便于学习
        // 初始化 IndexWriter
        // IndexWriterConfig config = new IndexWriterConfig(analyzer).setCodec(new Lucene80Codec());
        this.writer = new IndexWriter(directory, new IndexWriterConfig(analyzer));
    }

    // 写入，单条文档
    public void index(Document document) throws IOException {
        this.writer.addDocument(document);
    }

    // 写入，一次可以写多个文档
    public void batchIndex(List<Document> documents) throws IOException {
        this.writer.addDocuments(documents);
    }

    public void flush() throws IOException {
        this.writer.commit();
    }

    public List<DocInfo> matchQuery(String fieldName, String queryText, int topN) throws IOException, ParseException {
        QueryParser parser = new QueryParser(fieldName, analyzer);
        Query query = parser.parse(queryText);
        return search(query, topN);
    }

    public List<DocInfo> termQuery(String fieldName, String queryText, int topN) throws IOException {
        Query query = new TermQuery(new Term(fieldName, queryText));
        return search(query, topN);
    }

    private List<DocInfo> search(Query query, int topN) throws IOException {
        DirectoryReader reader = DirectoryReader.open(this.writer);
        IndexSearcher searcher = new IndexSearcher(reader);
        ScoreDoc[] topNDocs = searcher.search(query, topN).scoreDocs;

        List<DocInfo> docInfos = new ArrayList<>();
        for (ScoreDoc scoreDoc : topNDocs) {
            DocInfo docInfo = new DocInfo();
            docInfo.setDocId(scoreDoc.doc);
            docInfo.setContent(searcher.doc(scoreDoc.doc).get("content"));
            docInfo.setScore(scoreDoc.score);
            docInfos.add(docInfo);
        }
        reader.close();
        return docInfos;
    }

    public long ramBytesUsed() {
        return this.writer.ramBytesUsed();
    }

    public void close() throws IOException {
        this.writer.close();
        this.analyzer.close();
        this.directory.close();
   }
}
