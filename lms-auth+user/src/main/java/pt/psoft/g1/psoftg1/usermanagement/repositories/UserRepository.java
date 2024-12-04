/*
 * Copyright (c) 2022-2024 the original author or authors.
 *
 * MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package pt.psoft.g1.psoftg1.usermanagement.repositories;

import java.util.List;
import java.util.Optional;

import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.usermanagement.model.User;
import pt.psoft.g1.psoftg1.shared.services.Page;
import pt.psoft.g1.psoftg1.usermanagement.services.SearchUsersQuery;

/**
 *
 */
public interface UserRepository {

	<S extends User> List<S> saveAll(Iterable<S> entities);

	<S extends User> User save(S entity);

	Optional<User> findById(Long objectId);

	default User getById(final Long id) {
		final Optional<User> maybeUser = findById(id);
		// throws 404 Not Found if the user does not exist or is not enabled
		return maybeUser.filter(User::isEnabled).orElseThrow(() -> new NotFoundException(User.class, id));
	}

	Optional<User> findByUsername(String username);

	List<User> searchUsers(Page page, SearchUsersQuery query);

	List<User> findByNameName(String name);
	List<User> findByNameNameContains(String name);
	void delete(User user);
}
