package sejong.multimedia.example;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import sejong.multimedia.elastic.ElasticSearchConnection;
import static org.elasticsearch.common.xcontent.XContentFactory.*;

public class ElasticSearchExample01 {
  public static void main(String[] args) {
    try {
      ElasticSearchConnection conn = new ElasticSearchConnection("localhost");
      Client client = conn.getClient();

      IndexResponse response = client.prepareIndex("twitter", "_doc", "3")
          .setSource(jsonBuilder()
                      .startObject()
                          .field("user", "kimchy2")
                          .field("postDate", new Date())
                          .field("message", "trying out Elasticsearch")
                      .endObject()
                    )
          .get();
      client.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
