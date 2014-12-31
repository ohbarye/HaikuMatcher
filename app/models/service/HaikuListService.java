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
	public List<ObjectNode> getHaikusJsonByScreenName(String screenName) throws TwitterException {
        return toJsonObject(getHaikusByScreenName(screenName));	
	}
	/**
	 * 
	 * @param screenName
	 * @return
	 * @throws TwitterException
	 */
	public List<ObjectNode> getHaikusJsonByQuery(String query) throws TwitterException {
        return toJsonObject(getHaikusByQuery(query));	
	}
		
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
	public List<Status> getHaikusByQuery(String key) throws TwitterException {
        Query query = new Query();
        query.setQuery(key);
        query.setCount(100);

        List<Status> resultList = new ArrayList<>();
        
        for (int page=0; page < 10; page++) {
        	QueryResult result = TwitterUtil.search(query);
        	resultList.addAll(filter(result.getTweets()));
        	
        	if (resultList.size() > 5) {
        		break;
        	}

        	if (result.hasNext())	{
        		query = result.nextQuery();
        	} else {
        		break;
        	}
        }
        // 新しい順に並び替える
//        Collections.reverse(resultList);
		return resultList;
	}
	
	/**
	 * 
	 * @param screenName
	 * @return
	 * @throws TwitterException
	 */
	public List<Status> getHaikusByScreenName(String screenName) throws TwitterException {
		Paging paging = new Paging();
		paging.setCount(100);

        List<Status> resultList = new ArrayList<>();
        
        for (int page=1; page < 10; page++) {
    		paging.setPage(page);
        	List<Status> result = TwitterUtil.getUserTimeline(screenName, paging);
        	resultList.addAll(filter(result));
        	
        	if (resultList.size() > 10) {
        		break;
        	}
        }
        // 新しい順に並び替える
        Collections.reverse(resultList);
		return resultList;
	}
	
}
