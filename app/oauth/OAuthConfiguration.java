package oauth;

import java.util.List;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import play.Logger;

/**
 * Configuration for OAuth authentication/authorization
 * @author cmordue
 *
 */
public class OAuthConfiguration {

	private static final Configuration OAUTH_CONFIG;
	private static final Configuration OAUTH_CONFIG_DEFAULT;
	
	static {
		try {
			OAUTH_CONFIG = new PropertiesConfiguration("oauth.properties");
			Logger.info("Loaded oauth config.");
			OAUTH_CONFIG_DEFAULT = (OAUTH_CONFIG.getKeys("default").hasNext()) ?
					OAUTH_CONFIG.subset("default") : null;
		} catch (ConfigurationException e) {
			String message = "Failed to load oauth config. FAIL!";
			Logger.fatal(e, message);
			throw new RuntimeException(message, e);
		}
	}

	public static Configuration getInstance(String serviceName) {
		Configuration c = OAUTH_CONFIG.subset(serviceName);
		if (OAUTH_CONFIG_DEFAULT != null) {
			CompositeConfiguration composite = new CompositeConfiguration(c);
			composite.addConfiguration(OAUTH_CONFIG_DEFAULT);
			return composite;
		}
		return c;
	}
	
	public static List<String> getAllServiceNames() {
		return OAUTH_CONFIG.getList("serviceNames");
	}

	private OAuthConfiguration() {
	}
}
