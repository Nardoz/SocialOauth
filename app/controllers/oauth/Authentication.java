package controllers.oauth;

import models.oauth.IOAuthAccessPersistence;
import oauth.IAccessToken;
import oauth.IOAuthClient;
import oauth.OAuthClientFactory;

import org.jboss.netty.handler.codec.http.QueryStringEncoder;

import play.mvc.Controller;
import play.mvc.Router;

public class Authentication extends Controller {

    /** signin or link a new social account */
	public static void authenticate(String callback, String serviceName) throws Exception {
		// 1: get the request token
		IOAuthClient oauthClient = OAuthClientFactory.getOAuthClient(serviceName);
		QueryStringEncoder qse = new QueryStringEncoder(oauthClient.getCallbackURL());
		qse.addParam("callback", callback);
		qse.addParam("serviceName", serviceName);
		String callbackURL = qse.toString();
		oauthClient.authenticate(session, params, callbackURL);
	}

	public static void oauthCallback(String callback, String serviceName) throws Exception {
		// 2: get the access token
		IOAuthClient oauthClient = OAuthClientFactory.getOAuthClient(serviceName);
		IAccessToken token = oauthClient.retrieveAccessToken(session, params);
		
		// save the access token
		IOAuthAccessPersistence persistence = OAuthClientFactory.getAccessPersistence(serviceName);
		persistence.saveAccessToken(session, params, token, serviceName);
		
		if (callback == null || callback.isEmpty()) callback = Router.getFullUrl(request.controller + ".home");
		redirect(callback);
	}

}
