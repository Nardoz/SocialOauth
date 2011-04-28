package oauth;

import org.apache.commons.configuration.Configuration;

import play.mvc.Scope.Params;
import play.mvc.Scope.Session;

public interface IOAuthClient {
	
	public String getCallbackURL();
	
	public void initialize(final Configuration config);
	
	// oauth 2.0: step 1
	public RequestToken retrieveRequestToken(Session session, Params params, String callbackURL) throws Exception;

	// oauth 1.0: step 2
	// oauth 2.0: step 1
	public void authenticate(Session session, Params params, String callbackURL) throws Exception;

	/**
	 * Should retrieve the access token and clean up any data in the session involved 
	 * with the authentication/authorization.
	 * In OAuth 1.0 this is step 3 (of 3).
	 * In OAuth 2.0 this is step 2 (of 2).
	 * @param session
	 * @param params
	 * @return the access token
	 * @throws Exception
	 */
	public IAccessToken retrieveAccessToken(Session session, Params params) throws Exception;

}
