package oauth;

import org.apache.commons.configuration.Configuration;

import play.mvc.Scope.Params;
import play.mvc.Scope.Session;

public abstract class AbstractOAuthClient implements IOAuthClient {

	private String consumerKey;
	private String consumerSecret;
	private String callbackURL;
	
	public AbstractOAuthClient() {}
	
	public AbstractOAuthClient(Configuration c) {
		initialize(c);
	}
	
	@Override
	public void initialize(Configuration c) {
		callbackURL = c.getString("callbackURL");
		consumerKey = c.getString("consumerKey");
		consumerSecret = c.getString("consumerSecret");
	}
	
	protected String getConsumerKey() {
		return consumerKey;
	}

	protected String getConsumerSecret() {
		return consumerSecret;
	}
	
	public String getCallbackURL() {
		return callbackURL;
	}
	
	@Override
	public RequestToken retrieveRequestToken(Session session, Params params, String callbackURL) throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	public void authenticate(Session session, Params params, String callbackURL) throws Exception {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public IAccessToken retrieveAccessToken(Session session, Params params) throws Exception {
		throw new UnsupportedOperationException();
	}
}
