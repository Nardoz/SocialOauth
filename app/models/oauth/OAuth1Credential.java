package models.oauth;

import javax.persistence.Entity;

import oauth.OAuthAccessToken;

@Entity
public class OAuth1Credential extends OAuthServiceCredential {
	
	public static OAuth1Credential findOrCreate(String serviceName, OAuthAccessToken token) {
		OAuth1Credential c = OAuth1Credential.find(
				"token = ? and secret = ? and (serviceName = null or serviceName = ?)", 
				token.getToken(), 
				token.getSecret(), 
				serviceName).first();
		if (c == null) {
			c = new OAuth1Credential(serviceName, token);
			c.save();
		} else if (c.getServiceName() == null) {
			c.setServiceName(serviceName);
			c.save();
		}
		return c;
	}
	
	private String token;

	private String secret;
	
	public OAuth1Credential(String serviceName, OAuthAccessToken creds) {
		super(serviceName);
		this.secret = creds.getSecret();
		this.token = creds.getToken();
	}

	public void setToken(String token) {
		this.token = token;
		save();
	}

	public String getToken() {
		return token;
	}

	public void setSecret(String secret) {
		this.secret = secret;
		save();
	}

	public String getSecret() {
		return secret;
	}
}
