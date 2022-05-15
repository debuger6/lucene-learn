package example1;

import org.apache.lucene.document.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.util.BytesRef;

import javax.print.Doc;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, ParseException {
        // 准备文档
        String log = "The TF/IDF algorithm which used to be the default in Elasticsearch and Lucene. See Lucene’s Practical Scoring Function for more information.";
        Application application = new Application("./data");

        // 开始时间
        long startTime = System.currentTimeMillis();

        // 索引文档
        application.index(log);
        // 索引消耗时长
        System.out.println("index cost: " + (System.currentTimeMillis() - startTime) + "ms");
        // 索引花费内存
        System.out.println("memory used: " + application.ramBytesUsed()/1024 + "KB");
        application.flush();

        // match 查询
        List<DocInfo> docInfos = application.matchQuery("content", "Elasticsearch lucene", 10);
        System.out.println("matchQuery result:");
        for (DocInfo docInfo: docInfos) {
            System.out.println(docInfo.getContent());
        }

        // term 查询
        docInfos = application.termQuery("content", "Elasticsearch lucene", 10);
        System.out.println("termQuery result:");
        for (DocInfo docInfo: docInfos) {
            System.out.println(docInfo.getContent());
        }
    }
}
