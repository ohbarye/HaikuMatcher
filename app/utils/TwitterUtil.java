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

/**
 * Twitter API 関連のユーティリティークラス
 * @author ohbarye
 */
public class TwitterUtil {

	// Twitter インスタンスを生成しておく
	private static Twitter twitter = newTwitter();

	/**
	 * 設定ファイル情報を読み込んだ ConfigurationBuilder インスタンスを取得する。
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
	 * 設定ファイル情報を読み込んだ TwitterFactory インスタンスを取得する。
	 * @return
	 */
	public static TwitterFactory newTwitterFactory() {
		return new TwitterFactory(buildConfig().build());
	}

	/**
	 * 設定ファイル情報を読み込んだ Twitter インスタンスを取得する。
	 * @return
	 */
	public static Twitter newTwitter() {
		return newTwitterFactory().getInstance();
	}

	/**
	 * 指定したユーザのタイムラインを取得する。
	 * @return
	 * @throws TwitterException 
	 */
	public static List<Status> getUserTimeline(String screenName, Paging paging) throws TwitterException {
		return twitter.getUserTimeline(screenName, paging);
	}

	/**
	 * 指定した検索条件でツイートを検索する。
	 * @return
	 * @throws TwitterException 
	 */
	public static QueryResult search(Query query) throws TwitterException {
        return twitter.search(query);

	}
	
	/**
	 * ユーザが存在するかどうかを判定する。
	 * @return ユーザが存在するか
	 * @throws TwitterException 
	 */
	public static boolean exists(String screenName) throws TwitterException {
        try {
        	twitter.showUser(screenName);
        } catch(TwitterException te) {
        	// 404 の場合はユーザ不在
        	if (te.getStatusCode() == 404) {
        		return false;
        	} else if (te.getStatusCode() == 401) {
        		System.out.println("認証に失敗しました。");
        		throw te;
        	} else {
        		throw te;
        	}
        }
        return true;
	}
	
	/**
	 * 引数の screenName を持つユーザが公開ユーザかどうかを判定する。
	 * @return ユーザが公開ユーザかどうか
	 * @throws TwitterException 
	 */
	public static boolean isPublicUser(String screenName) throws TwitterException {
		try {
			return !twitter.showUser(screenName).isProtected();
	    } catch(TwitterException te) {
	    		System.out.println("認証に失敗しました。");
	    		throw te;
	    }
	}

	/**
	 * 引数の screenName を持つ公開ユーザが存在するか判定する。
	 * @return 公開ユーザが存在するか
	 * @throws TwitterException 
	 */
	public static boolean existsPublicUser(String screenName) throws TwitterException {
        return exists(screenName) && isPublicUser(screenName);
	}

}
