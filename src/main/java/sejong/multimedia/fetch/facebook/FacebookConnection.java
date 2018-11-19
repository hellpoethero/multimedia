package sejong.multimedia.fetch.facebook;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Version;
import com.restfb.types.User;

public class FacebookConnection {
  private FacebookClient client;
  private static FacebookConnection instance;
  private List<String> accessTokenList;
  public static void main(String[] args) {
    FacebookClient client = FacebookConnection.getInstance().getClient();
    
    User user = client.fetchObject("me", User.class);
    System.out.println("User name: " + user.getName());
  }
  
  private FacebookConnection() throws IOException {
    File accessTokenFile = new File("G:\\Hoc tap\\Sejong\\Multimedia\\multimedia/access_token.txt");
    FileReader fr = new FileReader(accessTokenFile);
    BufferedReader br = new BufferedReader(fr);

    String sCurrentLine;
    accessTokenList = new ArrayList<String>();
    while ((sCurrentLine = br.readLine()) != null) {
        accessTokenList.add(sCurrentLine);
    }
  }

  public static FacebookConnection getInstance() {
    try {
      if (instance == null) {
        instance = new FacebookConnection();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return instance;
  }

  
  public FacebookClient getClient() {
    Date date = new Date();
    Random rd = new Random(date.getTime());
    String accessToken = accessTokenList.get(rd.nextInt(accessTokenList.size()));
    client = new DefaultFacebookClient(accessToken, Version.VERSION_2_7);
    return client;
  }
}
