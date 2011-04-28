package oauth;

public class OAuthAccessToken implements IAccessToken {
	
	private String token;

	private String secret;

	public OAuthAccessToken(String token, String secret) {
		this.token = token;
		this.secret = secret;
	}
	
	public String getToken() {
		return token;
	}
	
	public String getSecret() {
		return secret;
	}
}
