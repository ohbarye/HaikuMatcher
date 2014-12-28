package controllers.api;

import javax.persistence.Transient;

import models.service.HaikuListService;

import com.fasterxml.jackson.databind.node.ObjectNode;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import twitter4j.TwitterException;
import utils.TwitterUtil;

public class HaikuList extends Controller {

	@Transient
	private static HaikuListService haikuListService = new HaikuListService();
	
	/**
	 * Top Page
	 * @return
	 * @throws TwitterException 
	 */
    public static Result index() throws TwitterException {    	
    	return ok(views.html.haikulist.index.render(
    			"ユーザー名を入れてください."
    			, TwitterUtil.searchByJa("バルス").getTweets()));
    }

	/**
	 * 入力されたユーザ名から575調の tweet を検索する
	 * @return
	 */
    public static Result myHaiku(String screenName) {

    	ObjectNode resultJson = Json.newObject();
		
		try {
	        resultJson.put("tweetList",
	        		Json.toJson(haikuListService.getHaikuTweetList(screenName)));
	        resultJson.put("result", "OK");
	        
    	} catch(TwitterException e) {
    		e.printStackTrace();
    		resultJson.put("result", "NG");
    	}

    	return ok(resultJson);

    }

}
