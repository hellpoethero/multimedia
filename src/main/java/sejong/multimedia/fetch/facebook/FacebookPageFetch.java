package sejong.multimedia.fetch.facebook;

import java.util.List;
import com.restfb.Connection;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.Page;
import com.restfb.types.Post;

public class FacebookPageFetch {
  static FacebookClient client = FacebookConnection.getInstance().getClient();
  public static void main(String[] args) {
    getPageInfo("theguardian");
    getPostsFromFollow("theguardian", 50);
  }
 
  public static void getPageInfo(String pageId) {
    Page page = client.fetchObject(pageId, Page.class,
        Parameter.with("fields", "name,id,fan_count,description,about"));
    System.out.println(page);
  }
  
  public static void getPostsFromFollow(String followId, int postNumber) {
    Connection<Post> feed =
        client.fetchConnection(followId + "/posts", Post.class, Parameter.with("limit", postNumber),
            Parameter.with("fields", "id, message, from, created_time"));
    System.out.println(feed);
  }
}
