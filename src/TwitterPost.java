import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by akshay on 2/5/16.
 */
public class TwitterPost {
    int numOfLikes;
    int numOfShares;
    String content;
    Date date;

    TwitterPost(int numOfLikes, int numOfShares, String content, Date date) {
        this.numOfLikes = numOfLikes;
        this.numOfShares = numOfShares;
        this.content = content;
        this.date = date;
    }

    public static void prettyPrint(List<TwitterPost> posts) {
        for(TwitterPost post: posts) {
            System.out.println("\n\ntweets{" +
                    " Name -> " + post.content + "\n" +
                    "Likes -> " + post.numOfShares + "\n" +
                    "Shares -> '" + post.numOfLikes + "\n" +
                    "}");
        }
    }

    public Date getDate() {
        return date;
    }

    public int getNumOfLikes() {
        return numOfLikes;
    }

    public int getNumOfShares() {
        return numOfShares;
    }
}


class TwitterApp {

    public static void main(String args[]) {

        ConfigurationBuilder cbObj = new ConfigurationBuilder();
        cbObj.setDebugEnabled(true)
                .setOAuthConsumerKey("JANWyjs9Wr9uMUhoCza8ZEryb")
                .setOAuthConsumerSecret("2RLG0zm7wUivNqrkqPs95HauyRGBDiz7nmN8V2RZxSeV2seKdN")
                .setOAuthAccessToken("3141976664-8IGQeAfJ7aVU0erGzwRcqwflPfX2KhjWu70y53W")
                .setOAuthAccessTokenSecret("cT9xpRSuGJSuV0FxUufamZMZgiD2MnRNbtY1YcEQLONDD");
        TwitterFactory factory = new TwitterFactory(cbObj.build());
        Twitter twitter = factory.getInstance();
        List<TwitterPost> posts = new ArrayList<>();
        Comparator<TwitterPost> dateCmp = (TwitterPost p1, TwitterPost p2) -> p1.getDate().compareTo(p2.getDate());
        Comparator<TwitterPost> likeCmp = (TwitterPost p1, TwitterPost p2) -> {
            if (p1.getNumOfLikes() > p2.getNumOfLikes()) return 1;
            else if(p1.getNumOfLikes() < p2.getNumOfLikes()) return -1;
            else return 0;
        };
        Comparator<TwitterPost> shareCmp = (TwitterPost p1, TwitterPost p2) -> {
            if (p1.getNumOfShares() > p2.getNumOfShares()) return 1;
            else if(p1.getNumOfShares() < p2.getNumOfShares()) return -1;
            else return 0;
        };

        try {
            ResponseList<Status> resList = twitter.getHomeTimeline();
            for (Status list : resList) {
                Date date = list.getCreatedAt();
                String content = list.getText();
                int likes = list.getFavoriteCount();
                int shares = list.getRetweetCount();
                posts.add(new TwitterPost(likes, shares, content, date));
            }
            System.out.println("Posts sorted by Date: ");
            posts.sort(dateCmp.reversed());
            TwitterPost.prettyPrint(posts);
            System.out.println("\n\nPosts sorted by Likes : ");
            posts.sort(likeCmp.reversed());
            TwitterPost.prettyPrint(posts);
            System.out.println("\n\nPosts sorted by Retweets : ");
            posts.sort(shareCmp.reversed());
            TwitterPost.prettyPrint(posts);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }
}