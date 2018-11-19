package sejong.multimedia.elastic;

import java.net.InetAddress;
import java.util.List;

import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

public class ElasticSearchConnection {
  private Client client;

  public ElasticSearchConnection(String ipAddress) throws Exception {
    this(ipAddress, "");
  }

  @SuppressWarnings("resource")
  public ElasticSearchConnection(String ipAddress, String cluster) throws Exception {
    Settings settings = Settings.builder().put("client.transport.sniff", true).build();

    if (!cluster.isEmpty())
      settings = Settings.builder().put("client.transport.sniff", true).put("cluster.name", cluster)
          .build();
    client = new PreBuiltTransportClient(settings)
        .addTransportAddress(new TransportAddress(InetAddress.getByName(ipAddress), 9300));
  }

  public void close() {
    client.close();
  }

  public Client getClient() {
    return client;
  }

  public void setClient(Client client) {
    this.client = client;
  }

  public void createIndexResponse(String indexname, String type, List<String> jsondata) {
    IndexRequestBuilder requestBuilder = client.prepareIndex(indexname, type);

    for (int i = 0; i < jsondata.size(); i++) {
      requestBuilder.setSource(jsondata.get(i), XContentType.JSON).get();
    }
  }

  public IndexResponse createIndexResponse(String indexname, String type, String jsondata) {
    IndexResponse response =
        client.prepareIndex(indexname, type).setSource(jsondata, XContentType.JSON).get();
    return response;
  }

  public IndexResponse createIndexResponse(String indexname, String type, String id,
      String jsondata) {
    IndexResponse response =
        client.prepareIndex(indexname, type, id).setSource(jsondata, XContentType.JSON).get();
    return response;
  }

  public IndexResponse createIndexResponse(String indexname, String type, String id, String parent,
      String jsondata) {
    IndexResponse response = client.prepareIndex(indexname, type, id)
        .setSource(jsondata, XContentType.JSON).setParent(parent).get();
    return response;
  }

  public void optimize(String indexname) {
    client.admin().indices().prepareForceMerge(indexname).setMaxNumSegments(1)
        .setOnlyExpungeDeletes(true).setFlush(true).get();
  }
}
