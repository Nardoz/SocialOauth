package oauth;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.commons.configuration.Configuration;

import play.Logger;
import play.libs.WS.WSRequest;
import play.mvc.Scope.Params;
import play.mvc.Scope.Session;
import play.mvc.results.Redirect;

public class OAuthClient extends AbstractOAuthClient {

	private String requestURL;
	private String accessURL;
	private String authorizeURL;

	public OAuthClient() {
		super();
	}
	
	public OAuthClient(Configuration c) {
		super(c);
		initialize(c);
	}

	public WSOAuthConsumer getConsumer(ICredentials cred) {
		WSOAuthConsumer consumer = new WSOAuthConsumer(
				getConsumerKey(),
				getConsumerSecret());
		consumer.setTokenWithSecret(cred.getToken(), cred.getSecret());
		return consumer;
	}

	public OAuthProvider getProvider() {
		OAuthProvider provider = new DefaultOAuthProvider(
				requestURL,
				accessURL,
				authorizeURL);
		provider.setOAuth10a(true);
		return provider;
	}
	
	protected String getAuthorizeURL() {
		return authorizeURL;
	}
	
	protected String getAccessURL() {
		return accessURL;
	}
	
	private static final String SKEY_TKN = "oauthclient_token";
	private static final String SKEY_TKN_SECRET = "oauthclient_tokensecret";
	
	public void authenticate(Session session, Params params, String callbackUrl) throws Exception {
		RequestToken rt = retrieveRequestToken(session, params, callbackUrl);
		session.put(SKEY_TKN, rt.getToken());
		session.put(SKEY_TKN_SECRET, rt.getSecret());
		throw new Redirect(rt.getAuthenticateUrl());
	}

	/**
	 * Retrieve the request token, and store it in user.
	 * to in order to get the token.
	 * @param cred the ICredentials where the oauth token and oauth secret will be set.
	 * @param callbackURL: the URL the user should be redirected after he grants the rights to our app
	 * @return the URL on the provider's site that we should redirect the user
	 */
	public RequestToken retrieveRequestToken(Session session, Params params, String callbackURL) throws Exception {
		ICredentials cred = new Credentials(getConsumerKey(), getConsumerSecret());
		OAuthConsumer consumer = getConsumer(cred);
		Logger.debug("Consumer key: " + consumer.getConsumerKey());
		Logger.debug("Consumer secret: " + consumer.getConsumerSecret());
		Logger.debug("Token before request: " + consumer.getToken());
		String authUrl = getProvider().retrieveRequestToken(consumer, callbackURL);
		Logger.debug("Token after request: " + consumer.getToken());
		Logger.debug("Token secret after request: " + consumer.getTokenSecret());
		return new RequestToken(consumer.getToken(), consumer.getTokenSecret(), authUrl);
	}

	/**
	 * Retrieve the access token, and store it in user.
	 * to in order to get the token.
	 * @param user the ICredentials with the request token and secret already set (using retrieveRequestToken).
	 * The access token and secret will be set these.
	 * @return the URL on the provider's site that we should redirect the user
	 * @see retrieveRequestToken
	 */
	public IAccessToken retrieveAccessToken(Session session, Params params) throws Exception {
		ICredentials userCred = new Credentials(session.get(SKEY_TKN), session.get(SKEY_TKN_SECRET));
		OAuthConsumer consumer = getConsumer(userCred);
		Logger.info("Token before retrieve: " + consumer.getToken());
		String verifier = params.get("oauth_verifier");
		Logger.info("Verifier: " + verifier);
		getProvider().retrieveAccessToken(consumer, verifier);
		return new OAuthAccessToken(consumer.getToken(), consumer.getTokenSecret());
	}

	// Signing requests

	/**
	 * Sign the url with the OAuth tokens for the user. This method can only be used for GET requests.
	 * @param url
	 * @return
	 * @throws OAuthMessageSignerException
	 * @throws OAuthExpectationFailedException
	 * @throws OAuthCommunicationException
	 */
	public String sign(ICredentials user, String url) throws Exception {
		return getConsumer(user).sign(url);
	}

	public WSRequest sign(ICredentials user, WSRequest request, String method) throws Exception {
		return getConsumer(user).sign(request, method);
	}

	@Override
	public void initialize(Configuration c) {
		super.initialize(c);
		requestURL = c.getString("requestURL");
		accessURL = c.getString("accessURL");
		authorizeURL = c.getString("authorizeURL");
	}

}
