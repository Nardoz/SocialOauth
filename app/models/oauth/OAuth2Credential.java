package models.oauth;

import javax.persistence.Entity;

import oauth.OAuth2AccessToken;

@Entity
public class OAuth2Credential extends OAuthServiceCredential {

	private String accessToken;
	
	public OAuth2Credential(String accessToken) {
		this.accessToken = accessToken;
	}
	
	public OAuth2Credential(String serviceName, OAuth2AccessToken accessToken) {
		super(serviceName);
		this.accessToken = accessToken.getAccessToken();
	}
	
	public String getAccessToken() {
		return accessToken;
	}

	public static OAuthServiceCredential findOrCreate(String serviceName, OAuth2AccessToken token) {
		OAuth2Credential c = OAuth2Credential.find(
				"accessToken = ? and (serviceName = null or serviceName = ?)", 
				token.getAccessToken(), 
				serviceName).first();
		if (c == null) {
			c = new OAuth2Credential(serviceName, token);
			c.save();
		} else if (c.getServiceName() == null) {
			c.setServiceName(serviceName);
			c.save();
		}
		return c;
	}
}
