package controllers.api;

import java.util.List;

import models.request.haikulist.User;
import play.*;
import play.data.Form;
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
    	Form<User> user = new Form(User.class);

    	Twitter twitter = TwitterUtil.newTwitter();

    	List<twitter4j.Status> statuses = null;
    	try {
	        Query query = new Query("バルス");
	        QueryResult result = twitter.search(query);
	        statuses = result.getTweets();
    	} catch(TwitterException e) {
    		e.printStackTrace();
    	}

    	return ok(views.html.haikulist.index.render("Your new application is ready.", user, statuses));
    }

	/**
	 * 入力されたユーザ名から575調の tweet を検索する
	 * @return
	 */
    public static Result search() {

    	Twitter twitter = TwitterUtil.newTwitter();
    	
    	try {
	        Query query = new Query("source:twitter4j yusukey");
	        QueryResult result = twitter.search(query);
	        for (twitter4j.Status status : result.getTweets()) {
	            System.out.println("@" + status.getUser().getScreenName() + ":" + status.getText());
	        }
    	} catch(TwitterException e) {
    		e.printStackTrace();
    	}
	        
    	Form<User> user = new Form(User.class).bindFromRequest();
    	if (!user.hasErrors()) {
            return ok(views.html.haikulist.index.render("Your new application is ready.", user, null));
    	} else {
    		return ok(views.html.haikulist.index.render("入力に問題があります。", user, null));
    	}
    }

}
