package utils;

import java.util.Date;
import java.util.List;

import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterUtil {

	private static Twitter twitter = newTwitter();

	/**
	 * 設定ファイル情報を読み込んだ ConfigurationBuilder インスタンスを取得する
	 * @return
	 */
	public static ConfigurationBuilder buildConfig() {
		return new ConfigurationBuilder()
				.setDebugEnabled(ConfigManager.getBoolean("debug"))
				.setOAuthConsumerKey(ConfigManager.getString("oauth.consumerKey"))
				.setOAuthConsumerSecret(ConfigManager.getString("oauth.consumerSecret"))
				.setOAuthAccessToken(ConfigManager.getString("oauth.accessToken"))
				.setOAuthAccessTokenSecret(ConfigManager.getString("oauth.accessTokenSecret"));
	}

	/**
	 * 設定ファイル情報を読み込んだ TwitterFactory インスタンスを取得する
	 * @return
	 */
	public static TwitterFactory newTwitterFactory() {
		return new TwitterFactory(buildConfig().build());
	}

	/**
	 * 設定ファイル情報を読み込んだ Twitter インスタンスを取得する
	 * @return
	 */
	public static Twitter newTwitter() {
		return newTwitterFactory().getInstance();
	}

	/**
	 * 
	 * @return
	 * @throws TwitterException 
	 */
	public static List<Status> getUserTimeline(String screenName, Paging paging) throws TwitterException {
		return twitter.getUserTimeline(screenName, paging);
	}

	/**
	 * 
	 * @return
	 * @throws TwitterException 
	 */
	public static QueryResult search(Query query) throws TwitterException {
        return twitter.search(query);

	}
	
	/**
	 * 
	 * @return
	 * @throws TwitterException 
	 */
	public static boolean exists(String screenName) throws TwitterException {
        try {
        	twitter.showUser(screenName);
        } catch(TwitterException te) {
        	if (te.getStatusCode() == 404) {
        		return false;
        	} else {
        		throw te;
        	}
        }
        return true;
	}
	/**
	 * 
	 * @return
	 * @throws TwitterException 
	 */
	public static boolean isPublicUser(User user) throws TwitterException {
        return !user.isProtected();
	}

	/**
	 * 
	 * @return
	 * @throws TwitterException 
	 */
	public static boolean existsPublicUser(String screenName) {
    	System.out.println("the request key is リクエストされたキーは  " + screenName);

    	boolean exists = false;
    	boolean isPublic = false;
    	
    	try {
    		System.out.println("ユーザーが存在するか確認します。check existence: " + screenName);
    		exists = exists(screenName);
    		System.out.println("ユーザーが存在するか確認しました。checked existence: " + exists);
    		
    		if (exists) {
	    		System.out.println("ユーザーが鍵か確認します。check is public: " + isPublic);
	    		isPublic = isPublicUser(twitter.showUser(screenName));
	    		System.out.println("ユーザーが鍵か確認しました。checked is public: " + isPublic);
    		}
    	} catch (TwitterException te) {
    		te.printStackTrace();
    	}
    	
    	return exists && isPublic;
	}

	public static void main(String[] args) throws TwitterException {
		twitter = new TwitterFactory( new ConfigurationBuilder()
		.setDebugEnabled(true)
		.setOAuthConsumerKey("8eTOF1ezXfib0iDmwcQKY1EG7")
		.setOAuthConsumerSecret("pgjDF0y1ApK4H8SCTRzVTFqtaQtUwPKQVVa0A0WqSNnFlU4JhE")
		.setOAuthAccessToken("126524303-qABwiQiwPkUeOuaZP8SO5UiOyST4XYLu6TodOG8F")
		.setOAuthAccessTokenSecret("HBp1tUErXTAh9M8gefr41pCiEwtcCYgWkh0UEnsnKY2Yg").build())
		.getInstance()
		;

		
		String key = "%E6%AD%A3%E6%9C%88";
        try {
        	System.out.println(exists(key) && isPublicUser(twitter.showUser(key)));
        } catch (Exception e) {
        	e.printStackTrace();
        }
	}
	
	
}
