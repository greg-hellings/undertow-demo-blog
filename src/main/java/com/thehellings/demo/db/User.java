package com.thehellings.demo.db;

import com.thehellings.demo.db.auto._User;
import io.undertow.security.idm.Account;

import java.security.Principal;
import java.util.Collections;
import java.util.Set;

public class User extends _User implements Account{

    private static final long serialVersionUID = 1L;
	private Principal principal;

	@Override
	public Principal getPrincipal() {
		if (this.principal == null) {
			this.principal = new UserPrincipal(this);
		}
		return this.principal;
	}

	@Override
	public Set<String> getRoles() {
		if (this.getIsAdmin()) {
			return Collections.singleton("admin");
		} else {
			return Collections.EMPTY_SET;
		}
	}

	/**
	 * A necessary evil of hosting the Account implementation.
	 */
	public class UserPrincipal implements Principal {
		private User user;

		public UserPrincipal(final User user) {
			this.user = user;
		}

		public String getName() {
			return this.user.getUsername();
		}
	}
}
