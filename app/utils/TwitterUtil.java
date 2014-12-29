package utils;

import java.util.List;

import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterUtil {

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
	public static List<Status> getUserTimeline(String screenName) throws TwitterException {
		Paging paging = new Paging();
		paging.setCount(100);
		return newTwitter().getUserTimeline(screenName, paging);
	}

	/**
	 * 
	 * @return
	 * @throws TwitterException 
	 */
	public static QueryResult search(Query query) throws TwitterException {
        return newTwitter().search(query);

	}
	
}
