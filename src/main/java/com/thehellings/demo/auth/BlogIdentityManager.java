package com.thehellings.demo.auth;

import com.thehellings.demo.Main;
import com.thehellings.demo.db.User;
import io.undertow.security.idm.Account;
import io.undertow.security.idm.Credential;
import io.undertow.security.idm.IdentityManager;
import io.undertow.security.idm.PasswordCredential;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.query.ObjectSelect;
import org.jboss.logging.Logger;

import java.util.List;

public class BlogIdentityManager implements IdentityManager {
    private static final Logger log = Logger.getLogger(BlogIdentityManager.class);

	/**
	 * Used to verify that an acount is still active and/or still has the same roles.
	 * <p>
	 *     In a production environment you will want to check here that the user has not been disabled, or possibly
	 *     check that the password is still up-to-date with what is cached, etc. You also might want to reload the
	 *     roles or permissions for the user to see if the user's access level has changed since their last login.
	 * </p>
	 * <p>
	 *     This implementation ignores all of that and assumes that, once you're in - you're in.
	 * </p>
	 *
	 * @param account
	 * @return
	 */
	@Override
	public Account verify(Account account) {
		return account;
	}

	/**
	 * The place where authentication against our database occurs.
	 * <p>
	 *     Since we're only doing username/password authentication here, I check these values are properly located within
	 *     the database, and return the User object if it is. Otherwise, we return null to indicate an authentication
	 *     failure.
	 * </p>
	 *
	 * @param id The username. Could be expanded to include other unique values instead, possibly
	 * @param credential The supplied authentication challenge response
	 * @return The User object if success, null if authentication failed
	 */
	@Override
	public Account verify(String id, Credential credential) {
        log.info("Looking for user " + id);
		if (credential instanceof PasswordCredential) {
			PasswordCredential passwordCredential = (PasswordCredential) credential;
            String password = new String(passwordCredential.getPassword());
			// Query database for User object
			ObjectContext context = Main.getRuntime().newContext();
			List<User> users = ObjectSelect.query(User.class).where(User.USERNAME.eq(id)).select(context);
			if (users.size() != 1) {
				return null;
			}
			User user = users.get(0);
			// Check password - real production code should have hashes here. We don't
			if (user.getPassword().equals(password)) {
				return user;
			}
		}
		// Either we're not in Basic/Form auth, or auth failed
		return null;
	}

	/**
	 * Not implemented because it's not involved in any of the work that we do. It's more used for X509 auth.
	 *
	 * @param credential
	 * @return
	 */
	@Override
	public Account verify(Credential credential) {
		return null;
	}
}
