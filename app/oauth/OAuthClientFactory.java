package oauth;

import java.util.concurrent.ConcurrentHashMap;

import models.oauth.IOAuthAccessPersistence;

import org.apache.commons.configuration.Configuration;


public class OAuthClientFactory {

	private static ConcurrentHashMap<String, IOAuthClient> oauthClients = new ConcurrentHashMap<String, IOAuthClient>();
	
	public static IOAuthClient getOAuthClient(String serviceName) {
		IOAuthClient client = oauthClients.get(serviceName);
		if (client == null) {
			Configuration c = OAuthConfiguration.getInstance(serviceName);
			if (c.containsKey("oauthClientClass")) {
				// User specific configuration
				String clientClassName = c.getString("oauthClientClass");
				client = classForName(clientClassName, IOAuthClient.class);
			}
			else if (c.getBoolean("isOAuth2", false)) {
				// standard oauth2.0 configuration
				client = new OAuth2Client();
			} else {
				// standard oauth1.0 configuration
				client = new OAuthClient();
			}
			if (client != null) {
				client.initialize(c);
				oauthClients.put(serviceName, client);
			}
		}
		return client;
	}
	
	private static <T> T classForName(String className, Class<T> clazz) {
		try {
			return clazz.cast(Class.forName(className).newInstance());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static IOAuthAccessPersistence getAccessPersistence(String serviceName) {
		Configuration c = OAuthConfiguration.getInstance(serviceName);
		String className = c.getString("oauthPersistenceClass");
		return classForName(className, IOAuthAccessPersistence.class);
	}
}
