package oauth;

public class Credentials implements ICredentials {
	
	private String token;

	private String secret;

	public Credentials(String token, String tokenSecret) {
		this.token = token;
		this.secret = tokenSecret;
	}
	
	@Override
	public String getSecret() {
		return secret;
	}

	@Override
	public String getToken() {
		return token;
	}

	@Override
	public void setSecret(String secret) {
		this.secret = secret;
	}

	@Override
	public void setToken(String token) {
		this.token = token;
	}

}
