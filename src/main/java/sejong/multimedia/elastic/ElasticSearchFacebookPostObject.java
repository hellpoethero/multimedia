package sejong.multimedia.elastic;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import java.io.IOException;
import org.elasticsearch.common.xcontent.XContentBuilder;
import com.restfb.types.Post;

public class ElasticSearchFacebookPostObject {
  public static XContentBuilder getObject(Post post) throws IOException {
    return jsonBuilder()
    .startObject()
        .field("content", post.getMessage())
        .field("postId", post.getId())
        .field("pageId", post.getFrom().getId())
        .field("link", post.getLink())
        .field("createdTime", post.getCreatedTime().getTime())
    .endObject();
  }
}
