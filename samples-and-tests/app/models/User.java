package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import models.oauth.OAuthServiceCredential;

import play.Logger;
import play.db.jpa.Model;

@Entity
public class User extends Model {

	@OneToMany
	public List<OAuthServiceCredential> credentials;
	
	public static User findOrCreate(OAuthServiceCredential c) {
		if (!c.isPersistent()) c.save();
		
		User u = User.find("select u from User u where ? in elements(credentials)", c).first();
		if (u == null) {
			Logger.info("No user found for credentials, creating one");
			u = new User();
			u.save();
			u.linkCredentials(c);
		} else {
			Logger.info("found user for credentials");
		}
		return u;
	}
	
	public User() {
		credentials = new ArrayList<OAuthServiceCredential>();
	}
	
	public void linkCredentials(OAuthServiceCredential c) {
		OAuthServiceCredential existing = null;
		for (OAuthServiceCredential creds : credentials) {
			if (creds.getServiceName().equals(c.getServiceName())) {
				// already have the service so delete the old one
				existing = creds;
				break;
			}
		}
		boolean add = true;
		boolean delete = false;
		if (existing != null) {
			if (!existing.getId().equals(c.getId())) {
				credentials.remove(existing);
				delete = true;
			} else {
				add = false;
			}
		}
		if (add) {
			credentials.add(c);
			save();
		}
		if (delete) {
			existing.delete();
		}
	}
	
	public static Long parseId(String userId) {
    	if (userId != null && !userId.isEmpty()) {
    		try {
    			return Long.parseLong(userId);
    		} catch (NumberFormatException e) {
    			Logger.warn(e, "Bad user id: " + userId);
    		}
    	}
    	return null;
	}
	
	public List<String> getLinkedServiceNames() {
		List<String> serviceNames = new ArrayList<String>();
		for (OAuthServiceCredential c : credentials) {
			serviceNames.add(c.getServiceName());
		}
		return serviceNames;
	}
	
}
