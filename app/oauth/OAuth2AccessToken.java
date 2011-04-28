package oauth;

public class OAuth2AccessToken implements IAccessToken {

	private String accessToken;
	
	public OAuth2AccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	public String getAccessToken() {
		return accessToken;
	}
}
