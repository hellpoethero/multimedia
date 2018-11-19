package sejong.multimedia.example;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Dblp {
	public static void main(String[] args) {
	    Document doc;
	    try {
	      doc = Jsoup.connect("https://dblp.org/db/journals/ml/ml107.html").get();
	      System.out.println(doc.title());
	      Element summary = doc.getElementsByClass("content__standfirst").get(0).getElementsByTag("p").get(0);
	      Elements contents = doc.getElementsByClass("content__article-body from-content-api js-article__body").get(0).getElementsByTag("p"); 
//	      System.out.println();
//	      for (Element content : contents) {
//	        System.out.println(content.text());
//	      }
	      // category
	      Element mainCategory = doc.getElementsByClass("subnav__item subnav__item--parent").first();
	      if (mainCategory != null) {
	        System.out.println(mainCategory.getElementsByTag("a").first().text());
	      }
	      // sub category
	      Element subCategory = doc.getElementsByClass("subnav__item subnav__item--current-section").first().getElementsByTag("a").first();
	      System.out.println(subCategory.text());
	      // time
	      Element time = doc.getElementsByClass("content__dateline").first().getElementsByTag("time").first();
	      System.out.println(time.attr("data-timestamp"));
	      // keywords
	      Elements keywords = doc.getElementsByClass("submeta__keywords").first().getElementsByClass("submeta__link");
	      for (Element keyword : keywords) {
	        System.out.println(keyword.text());
	      }
	      // topic
	      Elements topic = doc.getElementsByClass("content__section-label content__label").first().getElementsByClass("label__link-wrapper");
	      System.out.println(topic.first().text());
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	}
}
