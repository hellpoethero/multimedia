package sejong.multimedia.elastic;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import java.io.IOException;
import org.elasticsearch.common.xcontent.XContentBuilder;
import sejong.multimedia.model.GuardianArticle;

public class ElasticSearchArticleObject {

  public static void main(String[] args) {
  }
  
  public static XContentBuilder getObject(GuardianArticle article) throws IOException {
    return jsonBuilder()
    .startObject()
        .field("title", article.getTitle())
        .field("category", article.getCategory())
        .field("subCategory", article.getSubCategory())
        .field("summary", article.getSummary())
        .field("content", article.getContent())
        .field("keywords", article.getKeywords())
        .field("createdTime", article.getTime())
        .field("topic", article.getTopic())
        .field("serie", article.getSerie())
        .field("source", article.getSource())
        .field("url", article.getUrl())
    .endObject();
  }
}
