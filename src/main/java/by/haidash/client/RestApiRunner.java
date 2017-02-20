package by.haidash.client;

import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity;

import by.haidash.client.entity.ApiClient;

public class RestApiRunner {

	private static final Logger LOGGER = Logger.getLogger(RestApiRunner.class);

	public static final int TEST_PORT = 8080;
	public static final String TEST_HOST = "localhost";
	public static final String TEST_CONSUMER_KEY = "test_consumer_key";
	public static final String TEST_CONSUMER_SECRET = "test_consumer_secret_2016";

	public static void main(String[] arg) {
		final ApiClient client = new ApiClient(TEST_HOST, TEST_PORT);
		client.setCredentials(TEST_CONSUMER_KEY, TEST_CONSUMER_SECRET);
		client.init();

		ResponseEntity response = client.getUsers();

		LOGGER.info("Users: " + response.getBody());
	}

}
