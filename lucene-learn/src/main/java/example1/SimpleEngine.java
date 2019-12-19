package example1;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.codecs.simpletext.SimpleTextCodec;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.MMapDirectory;
import org.apache.lucene.store.SimpleFSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class SimpleEngine {
    // 用于索引的基本数据结构
    private IndexWriter writer;
    private Analyzer analyzer;
    private Directory directory;

    public SimpleEngine(String dataPath) throws IOException {
        // 使用标准分词器
        this.analyzer = new StandardAnalyzer();
        //this.directory = new MMapDirectory(Paths.get(dataPath));
        this.directory = new SimpleFSDirectory(Paths.get(dataPath));

        // 注：用 SimpleTextCodec 可以读懂数据文件内容，便于学习
        // 初始化 IndexWriter
        IndexWriterConfig config = new IndexWriterConfig(analyzer).setCodec(new SimpleTextCodec());
        this.writer = new IndexWriter(directory, config);
    }

    // 写入，一次可以写多个文档
    public void index(List<Document> documents) throws IOException {
        for (Document doc  : documents){
            this.writer.addDocument(doc);
        }
    }

    public void refresh() throws IOException {
        this.writer.flush();
    }

    public void flush() throws IOException {
        this.writer.commit();
    }

    // 查询
    public ScoreDoc[] search(String fieldName, String queryText, int topN) throws ParseException, IOException {
        // 初始化 IndexSearcher
        DirectoryReader reader = DirectoryReader.open(this.writer);
        //DirectoryReader reader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(reader);

        QueryParser parser = new QueryParser(fieldName, analyzer);
        Query query = parser.parse(queryText);


        ScoreDoc[] topNDocs = searcher.search(query, topN).scoreDocs;
        reader.close();
        return topNDocs;
    }

   public void close() throws IOException {
        this.writer.close();
        this.analyzer.close();
        this.directory.close();
   }
}
