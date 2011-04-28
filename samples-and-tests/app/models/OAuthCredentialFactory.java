package models;

import controllers.Application;
import oauth.IAccessToken;
import oauth.OAuth2AccessToken;
import oauth.OAuthAccessToken;
import models.oauth.IOAuthAccessPersistence;
import models.oauth.OAuth1Credential;
import models.oauth.OAuth2Credential;
import models.oauth.OAuthServiceCredential;
import play.mvc.Scope.Params;
import play.mvc.Scope.Session;

public class OAuthCredentialFactory implements IOAuthAccessPersistence {
	
	public static OAuthServiceCredential findOrCreate(String serviceName, IAccessToken token) {
		return null;
	}
	
	public OAuthCredentialFactory() { }

	@Override
	public void saveAccessToken(Session session, Params params, oauth.IAccessToken token, String serviceName) {
		OAuthServiceCredential c = null;
		if (token instanceof OAuthAccessToken) {
			OAuthAccessToken cred = (OAuthAccessToken)token;
			c = OAuth1Credential.findOrCreate(serviceName, cred);
		} else if (token instanceof OAuth2AccessToken) {
			OAuth2AccessToken cred = (OAuth2AccessToken)token;
			c = OAuth2Credential.findOrCreate(serviceName, cred);
		}
		if (c != null) linkWithAccount(session, c);
	}
	
	private static void linkWithAccount(Session session, OAuthServiceCredential credentials) {
		String userIdStr = session.get(Application.SKEY_USER_ID);
		Long userId = User.parseId(userIdStr);
		if (userId == null) {
			User u = User.findOrCreate(credentials);
			session.put(Application.SKEY_USER_ID, u.getId());
		} else {
			User u = User.findById(userId);
			u.linkCredentials(credentials);
		}
	}
}
