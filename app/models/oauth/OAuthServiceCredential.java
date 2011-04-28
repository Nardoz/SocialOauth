package models.oauth;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import play.db.jpa.Model;

@Entity(name="OAuthCredential")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public class OAuthServiceCredential extends Model {

	private String serviceName;
	
	public OAuthServiceCredential() {
	}
	
	public OAuthServiceCredential(String serviceName) {
		this.serviceName = serviceName;
	}
	
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	
	public String getServiceName() {
		return serviceName;
	}
}
