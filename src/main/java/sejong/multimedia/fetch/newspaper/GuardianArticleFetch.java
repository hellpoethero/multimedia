package sejong.multimedia.fetch.newspaper;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sejong.multimedia.elastic.ElasticSearchArticleObject;
import sejong.multimedia.elastic.ElasticSearchConnection;
import sejong.multimedia.model.GuardianArticle;

public class GuardianArticleFetch {
  static Client client;
  public static void main(String[] args) {
    try {
      ElasticSearchConnection conn = new ElasticSearchConnection("localhost");

      client = conn.getClient();
//      String link = "https://www.theguardian.com/science/2018/nov/15/coffee-tea-drink-choice-study-linked-genes-how-we-perceive-bitterness";
//      IndexResponse response = client.prepareIndex("articles", "article", String.valueOf(link.hashCode()))
//          .setSource(ElasticSearchArticleObject.getObject(getByLink(link)))
//          .get();
    getByCategory("https://www.theguardian.com/science/all");
      client.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public static List<GuardianArticle> getByCategory(String categoryLink) {
//    List<GuardianArticle> articles = new ArrayList<GuardianArticle>();
    try {
      Document doc;
      doc = Jsoup.connect(categoryLink).get();
      Elements links = doc.getElementsByClass("fc-item__link");
      for (Element link : links) {
        System.out.println(link.attr("href"));
        try {
          GuardianArticle article = getByLink(link.attr("href"));
          if (article != null) {
            IndexResponse response = client.prepareIndex("newspaper", "articles", String.valueOf(link.attr("href").hashCode()))
                .setSource(ElasticSearchArticleObject.getObject(article))
                .get();
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      Element currentPageIndex = doc.getElementsByClass("button button--small button--tertiary pagination__action is-active").first();
      int nextPageIndex = Integer.parseInt(currentPageIndex.text()) + 1;
      String nextPageUrl = "https://www.theguardian.com/science?page=" + nextPageIndex;
      System.out.println(nextPageUrl);
      getByCategory(nextPageUrl);
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    return null;
  }
  
  public static GuardianArticle getByLink(String link) throws IOException {
    GuardianArticle article = new GuardianArticle();
    Document doc;
    doc = Jsoup.connect(link).get();
     
    Elements title = doc.getElementsByClass("content__headline");
    article.setTitle(title.text());
    
    Elements summary = doc.getElementsByClass("content__standfirst");
    article.setSummary(summary.text());
    
    Elements contents = doc.getElementsByClass("content__article-body from-content-api js-article__body").first().getElementsByTag("p"); 
    String contentStr = "";
    for (Element content : contents) {
      contentStr += content.text()+'\n';
    }
    article.setContent(contentStr);

    Element subCategory = doc.getElementsByClass("subnav-link subnav-link--current-section").first();
    Element mainCategory = doc.getElementsByClass("subnav__item subnav__item--parent").first();
    if (mainCategory != null) {
      article.setCategory(mainCategory.text());
      article.setSubCategory(subCategory.text());
    } else {
      article.setCategory(subCategory.text());
    }
    
    Element time = doc.getElementsByClass("content__dateline").first().getElementsByTag("time").first();
    article.setTime(Long.parseLong(time.attr("data-timestamp")));
    
    Elements keywords = doc.getElementsByClass("submeta__keywords").first().getElementsByClass("submeta__link");
    List<String> keywordStrs = new ArrayList<String>();
    for (Element keyword : keywords) {
      keywordStrs.add(keyword.text());
    }
    article.setKeywords(keywordStrs);
    
    Element topic = doc.getElementsByClass("content__section-label content__label").first();
    if (topic != null) {
      article.setTopic(topic.text());
    }
    
    Element serie = doc.getElementsByClass("content__label__link").first();
    if (serie != null) {
      if (!serie.text().equals(topic.text())) {
        article.setSerie(serie.text());
      }
    }
    
    article.setSource("The Guardian");
    article.setUrl(link);
    
    return article;
  }
}
