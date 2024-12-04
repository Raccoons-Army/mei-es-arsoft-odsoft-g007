package pt.psoft.g1.psoftg1.usermanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.usermanagement.api.UserViewAMQP;
import pt.psoft.g1.psoftg1.usermanagement.model.Role;
import pt.psoft.g1.psoftg1.usermanagement.model.User;
import pt.psoft.g1.psoftg1.usermanagement.repositories.UserRepository;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepo;

	@Transactional
	public User create(UserViewAMQP userViewAMQP) {
		String username = userViewAMQP.getUsername();
		Set<String> authorities = userViewAMQP.getAuthorities();

		if (userRepo.findByUsername(username).isPresent()) {
			throw new ConflictException("User already exists!");
		}

		User user = new User(username);

		for (String role : authorities) {
			user.addAuthority(new Role(role));
		}

		return userRepo.save(user);
	}

	@Transactional
	public void delete(final String userId) {
		if (userRepo.findById(userId).isEmpty()) {
			throw new ConflictException("User doesn't exist!");
		}

		User user = userRepo.findById(userId).get();

		userRepo.delete(user);
	}

	public User findUser(final String userId) {
		if (userRepo.findById(userId).isEmpty()) {
			throw new ConflictException("User doesn't exist!");
		}

		return userRepo.findById(userId).get();
	}

	public User getAuthenticatedUser(Authentication authentication) {
		if (authentication == null || !(authentication.getPrincipal() instanceof Jwt jwt)) {
			throw new AccessDeniedException("User is not logged in");
		}

		// split is present because jwt is storing the id before the username, separated by a comma
		String loggedUsername = jwt.getClaimAsString("sub").split(",")[1];

		Optional<User> loggedUser = userRepo.findByUsername(loggedUsername);
		if (loggedUser.isEmpty()) {
			throw new AccessDeniedException("User is not logged in");
		}

		return loggedUser.get();
	}

}
