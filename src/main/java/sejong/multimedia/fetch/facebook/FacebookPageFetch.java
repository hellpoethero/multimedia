package sejong.multimedia.fetch.facebook;

import java.io.IOException;
import java.util.List;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import com.restfb.Connection;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.Page;
import com.restfb.types.Post;
import sejong.multimedia.elastic.ElasticSearchConnection;
import sejong.multimedia.elastic.ElasticSearchFacebookPostObject;

//246
public class FacebookPageFetch {
  static FacebookClient facebookClient = FacebookConnection.getInstance().getClient();
  static Client client;
  public static void main(String[] args) {
    ElasticSearchConnection conn;
    try {
      conn = new ElasticSearchConnection("localhost");
      client = conn.getClient();
//      getPageInfo("theguardian");
      getPostsFromFollow("theguardian", 50);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
 
  public static void getPageInfo(String pageId) {
    Page page = facebookClient.fetchObject(pageId, Page.class,
        Parameter.with("fields", "name,id,fan_count,description,about"));
    System.out.println(page);
  }
  
  public static void getPostsFromFollow(String followId, int postNumber) throws IOException {
    Connection<Post> feed =
        facebookClient.fetchConnection(followId + "/posts", Post.class, Parameter.with("limit", postNumber),
            Parameter.with("fields", "id, message, from, created_time, link"));
    for (Post post : feed.getData()) {
      IndexResponse response = client.prepareIndex("facebook", "posts", String.valueOf(post.getId()))
          .setSource(ElasticSearchFacebookPostObject.getObject(post))
          .get();
      System.out.println(post.getId());
    }
  }
}
