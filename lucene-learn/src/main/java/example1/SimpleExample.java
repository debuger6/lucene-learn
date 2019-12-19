package example1;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.codecs.simpletext.SimpleTextCodec;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SimpleExample {
    public static void main(String[] args) throws IOException, ParseException {
        // 准备文档
        List<Document> docs = new ArrayList<>();
        Document doc = new Document();
        String text = "The TF/IDF algorithm which used to be the default in Elasticsearch and Lucene. See Lucene’s Practical Scoring Function for more information.";
        doc.add(new Field("content", text, TextField.TYPE_STORED));
        docs.add(doc);

        SimpleEngine engine = new SimpleEngine("./data");
        // 索引文档
        engine.index(docs);

        // 查询
        ScoreDoc[] topDocs = engine.search("content", "elasticsearch", 10);
        for (ScoreDoc scoreDoc: topDocs) {
            System.out.println(scoreDoc);
        }
    }
}
