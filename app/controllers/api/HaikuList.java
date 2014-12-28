package controllers.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.node.ObjectNode;

import play.*;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import utils.TwitterUtil;
import views.html.*;

public class HaikuList extends Controller {

	/**
	 * Top Page
	 * @return
	 */
    public static Result index() {    	
    	Twitter twitter = TwitterUtil.newTwitter();

    	List<twitter4j.Status> statuses = new ArrayList<>();
    	try {
	        Query query = new Query();
	        query.setQuery("バルス");
	        query.setLang("ja");
	        QueryResult result = twitter.search(query);
	        statuses = result.getTweets();
    	} catch(TwitterException e) {
    		e.printStackTrace();
    	}

    	return ok(views.html.haikulist.index.render("input user name.", statuses));
    }

	/**
	 * 入力されたユーザ名から575調の tweet を検索する
	 * @return
	 */
    public static Result myHaiku(String screenName) {

    	ObjectNode resultJson = Json.newObject();
		
		try {
    		// 特定のユーザのツイートを取得する
	        List<twitter4j.Status> statuses = TwitterUtil.newTwitter().getUserTimeline(screenName);

	        // 新しい順に並び替える
	        Collections.reverse(statuses);

	        List<ObjectNode> tweetList = new ArrayList<>();
	        statuses.stream().forEach(t ->
	        	tweetList.add(
		        	Json.newObject()
		        		.put("user",t.getUser().getScreenName())
		        		.put("text", t.getText())
		        		.put("createdAt", t.getCreatedAt().toString())
	        	));

	        resultJson.put("tweetList", Json.toJson(tweetList));
	        resultJson.put("result", "OK");
	        
    	} catch(TwitterException e) {
    		e.printStackTrace();
    		resultJson.put("result", "NG");
    	}

    	return ok(resultJson);

    }

}
