package models.oauth;

import oauth.IAccessToken;
import play.mvc.Scope.Params;
import play.mvc.Scope.Session;

public interface IOAuthAccessPersistence {

	public void saveAccessToken(Session session, Params params, IAccessToken token, String serviceName);
	
}
