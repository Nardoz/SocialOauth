package oauth;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.commons.configuration.Configuration;
import org.jboss.netty.handler.codec.http.QueryStringEncoder;

import play.Logger;
import play.libs.WS;
import play.libs.WS.HttpResponse;
import play.libs.WS.WSRequest;
import play.libs.ws.WSUrlFetch;
import play.mvc.Router;
import play.mvc.Scope.Params;
import play.mvc.Scope.Session;
import play.mvc.results.Error;
import play.mvc.results.Redirect;

import com.google.gson.JsonElement;

public class OAuth2Client extends AbstractOAuthClient {
	
	private String accessUrl;
	private String authorizeUrl;
	
	public OAuth2Client() {
		super();
	}
	
	public OAuth2Client(Configuration c) {
		super(c);
		initialize(c);
	}
	
	@Override
	public void initialize(Configuration c) {
		super.initialize(c);
		accessUrl = c.getString("accessURL2");
		authorizeUrl = c.getString("authorizeURL2");
	}
	
	@Override
	public void authenticate(Session session, Params params, String callbackURL) {
		try {
			if (callbackURL.startsWith(getCallbackURL())) {
				Logger.error("Authenticating with a callback url that doesn't match "
						+ "the configured one. Configured: %s Actual: %s", getCallbackURL(), callbackURL);
			}
			String url = authorizeUrl + "?client_id=" + URLEncoder.encode(getConsumerKey(), "UTF-8") 
					+ "&response_type=code&redirect_uri=" + URLEncoder.encode(callbackURL, "UTF-8");
			throw new Redirect(url);
		} catch (UnsupportedEncodingException e) {
			Logger.error(e, "url encoding failed on callback string " + callbackURL);
			throw new Error("Error: please try again");
		}
	}
	
	@Override
	public IAccessToken retrieveAccessToken(Session session, Params params) throws Exception {
//		Map<String, Object> args = new HashMap<String, Object>();
//		args.put("grant_type", "authorization_code");
//		String code = params.get("code");
//		args.put("code", code);
//		args.put("client_id", getConsumerKey());
//		args.put("client_secret", getConsumerSecret());
//		args.put("redirect_uri", getCallbackURL());
//		String baseURL = accessUrl;
//		HttpResponse response = WS.url(baseURL).params(args).post();
//		QueryStringEncoder qse = new QueryStringEncoder(accessUrl);
//		qse.addParam("grant_type", "authorization_code");
		String code = params.get("code");
//		qse.addParam("code", code);
//		qse.addParam("client_id", getConsumerKey());
//		qse.addParam("client_secret", getConsumerSecret());
//		qse.addParam("redirect_uri", getCallbackURL());
//		String completeUrl = qse.toString();
//		HttpResponse response = WS.url(completeUrl).post();
		StringBuilder sb = new StringBuilder();
		sb.append(accessUrl);
		sb.append("?client_id=").append(URLEncoder.encode(getConsumerKey(), "UTF-8"));
		sb.append("&redirect_uri=").append(URLEncoder.encode(getCallbackURL(), "UTF-8"));
		sb.append("&client_secret=").append(URLEncoder.encode(getConsumerSecret(), "UTF-8"));
		sb.append("&code=").append(URLEncoder.encode(code, "UTF-8"));
//		sb.append("?client_id=").append(getConsumerKey());
//		sb.append("&redirect_uri=").append(getCallbackURL());
//		sb.append("&client_secret=").append(getConsumerSecret());
//		String pipe = URLEncoder.encode("|", "UTF-8");
//		String codePipe = code.replaceAll("\\|", pipe);
//		sb.append("&code=").append(codePipe);
//		String url = String.format(accessUrl + "?client_id=%s&redirect_uri=%s&client_secret=%s&code=%s", WS.encode(getConsumerKey()), WS.encode(getCallbackURL()), WS.encode(getConsumerSecret()), WS.encode(code));
//        WSUrlFetch ws = new WSUrlFetch();
//        HttpResponse response = ws.newRequest(url).post();
//        String responseStr = response.getString();
//		WSUrlFetch ws = new WSUrlFetch();
//		WSRequest request = ws.newRequest(url);
//		HttpResponse response = request.post();
////		sb.append("?grant_type=authorization_code");
//		Logger.info("QueryStringEncoded: %s", completeUrl);
//		Logger.info("EscapedParams:      %s", sb.toString());
//		String url = new URL(sb.toString()).toString();
		HttpResponse response = WS.url(sb.toString()).get();
		if (response.getStatus() == 200) {
			JsonElement jsonObj = response.getJson();
			String accessToken = jsonObj.getAsJsonObject().getAsJsonPrimitive("access_token").getAsString();
			return new OAuth2AccessToken(accessToken);
		}
		return null;
	}
}
