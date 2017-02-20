package by.haidash.server.service;

import java.util.List;

import by.haidash.server.model.User;

public interface UserService {

	/**
	 * Getting all users.
	 *
	 * @return {@link List} of {@link User}
	 */
	List<User> getUsers();

	/**
	 * Getting consumer secret by provided key.
	 *
	 * @param key consumer key
	 * @return consumer secret
	 */
	String getSecretByKey(String key);
}
