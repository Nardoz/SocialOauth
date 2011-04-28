package oauth;

public class RequestToken extends Credentials {
	
	private String authenticateUrl;
	
	public RequestToken(String token, String tokenSecret, String authenticateUrl) {
		super(token, tokenSecret);
		this.authenticateUrl = authenticateUrl;
	}

	public String getAuthenticateUrl() {
		return authenticateUrl;
	}

}
