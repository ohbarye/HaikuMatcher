package models.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import play.libs.Json;
import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.TwitterException;
import utils.ConfigManager;
import utils.HaikuUtil;
import utils.TwitterUtil;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Service Class For HaikuList
 * @author ohbarye
 */
public class HaikuListService {
	
	/** リトライ回数 */
	private static final int RETRY_TIMES = ConfigManager.getInt("retry.times");
	/** 最低取得件数 */
	private static final int MINIMUM_REQUIRED = ConfigManager.getInt("minimun.required");

	/**
	 * 俳句っぽいツイートを検索する。
	 * <pre>
	 * 検索キーワードが…
	 * 公開ユーザ名であれば userTimeline から取得する。
	 * 公開ユーザ名でなければ query で取得する。
	 * </pre>
	 * @param key 検索キーワード
	 * @return haikus
	 * @throws TwitterException
	 */
	public List<ObjectNode> getHaikusJson(String key) throws TwitterException {
		if (TwitterUtil.existsPublicUser(key)) {
			return toJsonObject(getHaikusByScreenName(key));				
		} else {
			return toJsonObject(getHaikusByQuery(key));
		}
	}

	/**
	 * レスポンスJSON用の ObjectNodeリストを生成する。
	 * @param statuses
	 * @return レスポンスJSON用の ObjectNodeリスト
	 */
	public List<ObjectNode> toJsonObject(List<Status> statuses) {
        List<ObjectNode> tweetList = new ArrayList<>();
        statuses.stream().forEach(t ->
        	tweetList.add(
	        	Json.newObject()
	        		.put("name", t.getUser().getName())
	        		.put("screenName", t.getUser().getScreenName())
	        		.put("profileImageUrl", t.getUser().getProfileImageURL())
	        		.put("text", t.getText())
	        		.put("createdAt", t.getCreatedAt().toString())
        	));
        return tweetList;
	}
	
	/**
	 * ツイートのリストから俳句っぽいものだけを抽出する。
	 * @param statuses ツイートリスト
	 * @return 俳句っぽいツイートリスト
	 */
	public List<Status> filter(List<Status> statuses) {
		return statuses.stream()
				.filter(s -> HaikuUtil.isHaiku(s.getText()))
				.collect(Collectors.toList());
	}
	
	/**
	 * Queryで俳句っぽいツイートリストを取得する。
	 * @param key 検索キーワード
	 * @return 俳句っぽいツイートリスト
	 * @throws TwitterException
	 */
	public List<Status> getHaikusByQuery(String key) throws TwitterException {
        Query query = new Query();
        query.setQuery(key);
        query.setCount(100);

        List<Status> resultList = new ArrayList<>();

        // 指定した回数までリトライ
        for (int page=0; page < RETRY_TIMES; page++) {
        	// Twitter API でツイートリストを取得
        	QueryResult result = TwitterUtil.search(query);
        	// 俳句っぽいツイートだけを抽出してリストに追加
        	resultList.addAll(filter(result.getTweets()));
        	
        	// 5件以上取得できたら終了する
        	if (resultList.size() >= MINIMUM_REQUIRED) {
        		break;
        	}
        	// 検索結果が無くなったら終了する
        	if (result.hasNext())	{
        		query = result.nextQuery();
        	} else {
        		break;
        	}
        }
        // 新しい順に並び替えて返却
        Collections.reverse(resultList);
		return resultList;
	}
	
	/**
	 * 指定したユーザのタイムラインから俳句っぽいツイートリストを取得する。
	 * @param screenName ユーザ名
	 * @return 俳句っぽいツイートリスト
	 * @throws TwitterException
	 */
	public List<Status> getHaikusByScreenName(String screenName) throws TwitterException {
		Paging paging = new Paging();
		paging.setCount(100);

        List<Status> resultList = new ArrayList<>();
        
        // 指定した回数までリトライ
        for (int page=1; page < RETRY_TIMES; page++) {
        	// Twitter API でツイートリストを取得
    		paging.setPage(page);
        	List<Status> result = TwitterUtil.getUserTimeline(screenName, paging);
        	// 俳句っぽいツイートだけを抽出してリストに追加
        	resultList.addAll(filter(result));
        	
        	// 5件以上取得できたら終了する
        	if (resultList.size() >= MINIMUM_REQUIRED) {
        		break;
        	}
        }
        // 新しい順に並び替えて返却
        Collections.reverse(resultList);
		return resultList;
	}
	
}
