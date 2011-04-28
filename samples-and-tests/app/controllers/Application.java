package controllers;

import java.util.List;

import models.User;
import oauth.OAuthConfiguration;
import play.Logger;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Router;


public class Application extends Controller {
	
	public static final String SKEY_USER_ID = "UserID";

	/** security checks */
	@Before(unless = { "index" })
	public static void signinCheck() {
		Long userId = getUserId();
		if (userId == null) {
			redirect(request.controller + ".index", request.url);
		}
	}

	/** splash screen and login page */
    public static void index(String redirectUrl) {
    	if (redirectUrl == null) redirectUrl = Router.reverse(request.controller + ".showAdd").url;
    	List serviceNames = OAuthConfiguration.getAllServiceNames();
        render(serviceNames, redirectUrl);
    }
    
    public static void signout() {
    	session.clear();
    	index(null);
    }
    
    /** add a new place to follow */
    public static void showAdd() {
    	User u = getUser();
    	List<String> serviceNames = u.getLinkedServiceNames();
    	render(serviceNames);
    }
    
    private static Long getUserId() {
    	String userId = session.get(SKEY_USER_ID);
    	return User.parseId(userId);
    }
    
    private static User getUser() {
    	Long userId = getUserId();
    	return User.findById(userId);
    }
	
}