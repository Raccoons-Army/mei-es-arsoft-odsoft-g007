package pt.psoft.g1.psoftg1.usermanagement.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
public class User implements UserDetails {

	private String pk;

	private String username;

	private boolean enabled = true;

	private Set<Role> authorities = new HashSet<>();

	Long version;

	public User(final String username) {
		this.username = username;
	}

	public User() {
		// for ORM only
	}

	// for mapstruct
	public static User newUser(final String pk, final String username, final Set<Role> roles, Long version) {
		final var u = new User(username);
		u.setPk(pk);
		u.setAuthorities(roles);
		u.setVersion(version);
		return u;
	}

	public void addAuthority(final Role r) {
		authorities.add(r);
	}

	@Override
	public String getPassword() {
		return "";
	}

	@Override
	public boolean isAccountNonExpired() {
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return false;
	}
}
