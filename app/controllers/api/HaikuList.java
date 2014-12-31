package controllers.api;

import java.util.Collections;
import java.util.List;

import javax.persistence.Transient;

import models.service.HaikuListService;

import com.fasterxml.jackson.databind.node.ObjectNode;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import twitter4j.TwitterException;

/**
 * Haiku Matcher トップページのController
 * @author ohbarye
 */
public class HaikuList extends Controller {

	@Transient
	private static HaikuListService haikuListService = new HaikuListService();
	
	private static String INDEX_SEARCH_KEY = "俳句";
	
	/**
	 * Haiku Matcher トップページの初期表示処理
	 * @return top page
	 * @throws TwitterException 
	 */
    public static Result index() throws TwitterException {
    	// 初期表示用の検索ワードでツイートを取得して表示する
    	List<twitter4j.Status> tweets = haikuListService.getHaikusByQuery(INDEX_SEARCH_KEY);
    	Collections.reverse(tweets);
       	return ok(views.html.haikulist.index.render(
    			"任意のキーワードを入力して検索してください。", tweets));
    }

	/**
	 * 入力されたユーザ名または単語から575調のツイートを検索し、JSON型で返す。
	 * Haiku Matcher トップページの検索ボタン押下時の処理
	 * @return haikus
	 */
    public static Result myHaiku(String key) {
    	// screenNameの@は不要なので除去する
    	key = key.replaceAll("@", "");
    	ObjectNode resultJson = Json.newObject();
		
		try {
			// 俳句っぽいツイートを取得する
		    resultJson.put("tweetList", Json.toJson(haikuListService.getHaikusJson(key)));							
	        resultJson.put("result", "OK");	        
    	} catch(TwitterException e) {
    		e.printStackTrace();
    		resultJson.put("result", "NG");
    	}

    	return ok(resultJson);
    }
}
