package pt.psoft.g1.psoftg1.usermanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.psoft.g1.psoftg1.shared.services.Page;
import pt.psoft.g1.psoftg1.usermanagement.model.Librarian;
import pt.psoft.g1.psoftg1.usermanagement.model.Reader;
import pt.psoft.g1.psoftg1.usermanagement.model.Role;
import pt.psoft.g1.psoftg1.usermanagement.model.User;
import pt.psoft.g1.psoftg1.usermanagement.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService{

	private final UserRepository userRepo;
	private final EditUserMapper userEditMapper;

	@Transactional
	public void create(final CreateUserRequest request) {
		if (userRepo.findByUsername(request.getUsername()).isPresent()) {
			return;
		}


		User user;
		switch(request.getRole()) {
			case Role.READER: {
				user = Reader.newReader(request.getUsername());
				break;
			}
			case Role.LIBRARIAN: {
				user = Librarian.newLibrarian(request.getUsername());
				break;
			}
			default: {
				return;
			}
		}

		userRepo.save(user);
	}

	@Transactional
	public User update(final Long id, final EditUserRequest request) {
		final User user = userRepo.getById(id);
		userEditMapper.update(request, user);

		return userRepo.save(user);
	}

	@Transactional
	public User delete(final Long id) {
		final User user = userRepo.getById(id);

		user.setEnabled(false);
		return userRepo.save(user);
	}

	public boolean usernameExists(final String username) {
		return userRepo.findByUsername(username).isPresent();
	}

	public User getUser(final Long id) {
		return userRepo.getById(id);
	}

	public Optional<User> findByUsername(final String username) { return userRepo.findByUsername(username); }

	public List<User> searchUsers(Page page, SearchUsersQuery query) {
		if (page == null) {
			page = new Page(1, 10);
		}
		if (query == null) {
			query = new SearchUsersQuery("", "");
		}
		return userRepo.searchUsers(page, query);
	}

	public User getAuthenticatedUser(Authentication authentication) {
		if (authentication == null || !(authentication.getPrincipal() instanceof Jwt jwt)) {
			throw new AccessDeniedException("User is not logged in");
		}

		// split is present because jwt is storing the id before the username, separated by a comma
        String loggedUsername = jwt.getClaimAsString("sub").split(",")[1];

		Optional<User> loggedUser = findByUsername(loggedUsername);
		if (loggedUser.isEmpty()) {
			throw new AccessDeniedException("User is not logged in");
		}

		return loggedUser.get();
	}
}
