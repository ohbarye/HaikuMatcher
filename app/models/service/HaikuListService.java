package models.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import play.libs.Json;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.TwitterException;
import utils.HaikuUtil;
import utils.TwitterUtil;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * 
 * @author ohbarye
 *
 */
public class HaikuListService {
	
	/**
	 * 
	 * @param screenName
	 * @return
	 * @throws TwitterException
	 */
	public List<Status> getHaikuTweetList(List<Status> statuses) throws TwitterException {
		// 575調の tweet リストを取得する
		List<Status> haikuTweetList = filter(statuses);
		
        // 新しい順に並び替える
        Collections.reverse(haikuTweetList);

        return haikuTweetList;	
	}

	/**
	 * 
	 * @param screenName
	 * @return
	 * @throws TwitterException
	 */
	public List<ObjectNode> getHaikuTweetListJson(String screenName) throws TwitterException {
        return toJsonObject(getHaikuTweetList(getTweetList(screenName)));	
	}
	
	/**
	 * 
	 * @param screenName
	 * @return
	 * @throws TwitterException
	 */
	public List<Status> getTweetList(String screenName) throws TwitterException {
		// 特定のユーザのツイートを取得する
        return TwitterUtil.getUserTimeline(screenName);
	}
	
	public List<ObjectNode> toJsonObject(List<Status> statuses) {
        List<ObjectNode> tweetList = new ArrayList<>();
        statuses.stream().forEach(t ->
        	tweetList.add(
	        	Json.newObject()
	        		.put("user",t.getUser().getScreenName())
	        		.put("text", t.getText())
	        		.put("createdAt", t.getCreatedAt().toString())
        	));
        return tweetList;
	}
	
	public List<?> reverse(List<?> list) {
		Collections.reverse(list);
		return list;
	}
	
	/**
	 * 
	 * @param statuses
	 * @return
	 */
	public List<Status> filter(List<Status> statuses) {
		return statuses.stream()
				.filter(s -> HaikuUtil.isHaiku(s.getText()))
				.collect(Collectors.toList());
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 * @throws TwitterException
	 */
	public List<Status> getIndexHaikuTweetList(String key) throws TwitterException {
        Query query = new Query();
        query.setQuery(key);
        query.setLang("ja");
        query.setCount(100);

        List<Status> resultList = new ArrayList<>();
        
        for (int page=0; page < 10; page++) {
        	QueryResult result = TwitterUtil.search(query);
        	resultList.addAll(getHaikuTweetList(result.getTweets()));
        	
        	if (resultList.size() > 5) {
        		break;
        	}
        	
        	query = result.nextQuery();
        }
        

		return resultList;
	}
	
}
