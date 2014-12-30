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
    	String key = "俳句";
    	return ok(views.html.haikulist.index.render(
    			"575調に近い文章を検索して表示します。判定はわりと雑です。"
    			, haikuListService.getHaikusByQuery(key)));
    }

	/**
	 * 入力されたユーザ名または単語から575調の tweet を検索する
	 * @return
	 */
    public static Result myHaiku(String key) {
    	
    	key = key.replaceAll("@", "");
    	ObjectNode resultJson = Json.newObject();
		
		try {
			if (TwitterUtil.existsPublicUser(key)) {
		        resultJson.put("tweetList",
		        		Json.toJson(haikuListService.getHaikusJsonByScreenName(key)));				
			} else {
		        resultJson.put("tweetList",
		        		Json.toJson(haikuListService.getHaikusJsonByQuery(key)));
			}
			
	        resultJson.put("result", "OK");
	        
    	} catch(TwitterException e) {
    		e.printStackTrace();
    		resultJson.put("result", "NG");
    	}

    	return ok(resultJson);

    }

}
