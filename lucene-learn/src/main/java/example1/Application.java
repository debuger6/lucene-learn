package example1;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Application {
    private Engine engine;

    public Application(String dataPath) throws IOException {
        this.engine = new Engine(dataPath);
    }

    public void index(String log) throws IOException {
        Document document = new Document();
        document.add(new TextField("content", log, Field.Store.YES));
        this.engine.index(document);
    }

    public void batchIndex(List<String> logs) throws IOException {
        List<Document> documents = new ArrayList<>();
        logs.forEach(log -> {
            Document document = new Document();
            document.add(new TextField("content", log, Field.Store.YES));
            documents.add(document);
        });
        this.engine.batchIndex(documents);
    }

    public void flush() throws IOException {
        this.engine.flush();
    }

    public List<DocInfo> matchQuery(String fieldName, String queryText, int topN) throws IOException, ParseException {
        return this.engine.matchQuery(fieldName, queryText, topN);
    }

    public List<DocInfo> termQuery(String fieldName, String queryText, int topN) throws IOException {
        return this.engine.termQuery(fieldName, queryText, topN);
    }

    public long ramBytesUsed() {
        return this.engine.ramBytesUsed();
    }

    public void close() throws IOException {
        this.engine.close();
    }
}
