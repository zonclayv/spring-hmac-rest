package by.haidash.client.entity;

import org.apache.http.HttpHost;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.net.URI;

public class HttpClientRequestFactory extends HttpComponentsClientHttpRequestFactory {

	private final HttpHost host;
	private CredentialsProvider credentialsProvider;

	public HttpClientRequestFactory(HttpHost host) {
		super();
		this.host = host;
	}

	protected HttpContext createHttpContext(HttpMethod httpMethod, URI uri) {
		return createHttpContext();
	}

	public void setCredentialsProvider(CredentialsProvider credentialsProvider) {
		this.credentialsProvider = credentialsProvider;
	}

	private HttpContext createHttpContext() {
		final BasicScheme basicAuth = new BasicScheme();
		final AuthCache authCache = new BasicAuthCache();
		authCache.put(host, basicAuth);

		final BasicHttpContext localContext = new BasicHttpContext();
		localContext.setAttribute(HttpClientContext.AUTH_CACHE, authCache);
		localContext.setAttribute(HttpClientContext.CREDS_PROVIDER, credentialsProvider);

		return localContext;
	}
}
