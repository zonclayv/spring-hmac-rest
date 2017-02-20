package by.haidash.server.service;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import by.haidash.server.model.User;

import static ru.instinctools.server.model.tables.Users.USERS;

@Service
public class DefaultUserService implements UserService {

	@Autowired
	private DSLContext dsl;

	@Override
	public List<User> getUsers() {
		return dsl.selectFrom(USERS).fetchInto(User.class);
	}

	@Override
	public String getSecretByKey(String key) {
		return dsl.selectFrom(USERS).where(USERS.CONSUMERKEY.eq(key)).fetchOne(USERS.CONSUMERSECRET);
	}
}
