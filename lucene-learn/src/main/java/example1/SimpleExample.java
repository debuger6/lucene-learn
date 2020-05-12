package example1;

import org.apache.lucene.document.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.util.BytesRef;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SimpleExample {
    public static void main(String[] args) throws IOException, ParseException {
        // 准备文档
        List<Document> docs = new ArrayList<>();
        Document doc = new Document();
        String text = "The TF/IDF algorithm which used to be the default in Elasticsearch and Lucene. See Lucene’s Practical Scoring Function for more information.";
        doc.add(new Field("_source", text, TextField.TYPE_STORED));
        doc.add(new DoubleDocValuesField("avg_value_docvalue", 1.99));
        doc.add(new DoublePoint("avg_value_point", 2.89));
        doc.add(new SortedSetDocValuesField("name", new BytesRef("Jack")));
        doc.add(new StringField("_source2", text, Field.Store.YES));
        docs.add(doc);

        doc = new Document();
        doc.add(new SortedSetDocValuesField("name", new BytesRef("Tom")));
        docs.add(doc);

        doc = new Document();
        doc.add(new SortedSetDocValuesField("name", new BytesRef("Pony")));
        docs.add(doc);

        SimpleEngine engine = new SimpleEngine("./data");
        // 索引文档
        engine.index(docs);

        engine.refresh();
        //engine.flush();

        // 查询
        ScoreDoc[] topDocs = engine.search("_source", "Elasticsearch", 10);
        for (ScoreDoc scoreDoc: topDocs) {
            System.out.println(scoreDoc);
        }
    }
}
