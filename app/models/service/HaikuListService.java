package models.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import play.libs.Json;
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
	
	public List<ObjectNode> getHaikuTweetList(String screenName) throws TwitterException {
		
		// 575調の tweet リストを取得する
		List<Status> haikuTweetList = filter(getTweetList(screenName));
		
        // 新しい順に並び替える
        Collections.reverse(haikuTweetList);

        return toJsonObject(haikuTweetList);
	
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
	
	

	public List<Status> filter(List<Status> statuses) {
		return statuses.stream()
				.filter(s -> HaikuUtil.isHaiku(s.getText()))
				.collect(Collectors.toList());
	}
	
}
