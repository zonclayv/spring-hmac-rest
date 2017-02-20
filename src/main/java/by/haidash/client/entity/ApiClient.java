package by.haidash.client.entity;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.UUID;

import by.haidash.server.config.hmac.HmacSignatureBuilder;

public class ApiClient {

	private static final String USER_AGENT = "RestAPI client v.1.0";
	private final CredentialsProvider credentialsProvider;
	private final String scheme;
	private final String host;
	private final int port;
	private RestTemplate restTemplate;

	public ApiClient(String scheme, String host, int port) {
		this.credentialsProvider = new BasicCredentialsProvider();
		this.scheme = scheme;
		this.host = host;
		this.port = port;
	}

	public ApiClient(String host, int port) {
		this((port == 443) ? "https" : "http", host, port);
	}

	public ResponseEntity getUsers() {

		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setDate(Instant.now().toEpochMilli());
		headers.add(HttpHeaders.USER_AGENT, USER_AGENT);

		final AuthScope authScope = new AuthScope(host, port, AuthScope.ANY_REALM, scheme);
		final Credentials credentials = credentialsProvider.getCredentials(authScope);

		if (credentials == null) {
			throw new RuntimeException("Can't find credentials for AuthScope: " + authScope);
		}

		final String apiKey = credentials.getUserPrincipal().getName();
		final String apiSecret = credentials.getPassword();
		final String nonce = UUID.randomUUID().toString();
		final String dateString = headers.getFirst(HttpHeaders.DATE);
		final String resource = "/api/users";
		final HmacSignatureBuilder signatureBuilder = new HmacSignatureBuilder()
				.method("GET")
				.apiKey(apiKey)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.nonce(nonce)
				.date(dateString)
				.apiSecret(apiSecret);

		final String signature = signatureBuilder.buildAsBase64String();

		final String authHeader = signatureBuilder.getAlgorithm() + " " + apiKey + ":" + nonce + ":" + signature;
		headers.add(HttpHeaders.AUTHORIZATION, authHeader);

		final HttpEntity<String> entity = new HttpEntity<>(headers);
		final String url = scheme + "://" + host + ":" + port + resource;

		return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
	}

	public void setCredentials(String user, String password) {
		final AuthScope authScope = new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM);
		final UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(user, password);
		credentialsProvider.setCredentials(authScope, credentials);
	}

	public void init() {
		final HttpHost httpHost = new HttpHost(host, port, scheme);
		final HttpClientRequestFactory requestFactory = new HttpClientRequestFactory(httpHost);
		requestFactory.setCredentialsProvider(credentialsProvider);

		restTemplate = new RestTemplate(requestFactory);
	}
}
