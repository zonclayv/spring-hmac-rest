package by.haidash.server.config.hmac;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import by.haidash.server.service.UserService;

/**
 * Custom access filter for incoming requests.
 */
@Component
public class HmacAccessFilter extends OncePerRequestFilter {

	private static final Logger LOGGER = Logger.getLogger(HmacAccessFilter.class);
	private static final Pattern AUTHORIZATION_HEADER_PATTERN = Pattern.compile("^(\\w+) (\\S+):(\\S+):([\\S]+)$");

	@Autowired
	private UserService userService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		final AuthorizationHeader authHeader = getAuthHeader(request);
		if (authHeader == null) {
			LOGGER.warn("Authorization header is missing");
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		final String apiKey = authHeader.getApiKey();
		if (apiKey == null) {
			// invalid digest
			LOGGER.error("Invalid API key");
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid authorization data");
			return;
		}

		final String consumerSecret = userService.getSecretByKey(apiKey);
		final byte[] apiSecret = consumerSecret.getBytes(StandardCharsets.UTF_8);

		final HmacSignatureBuilder signatureBuilder = new HmacSignatureBuilder()
				.algorithm(authHeader.getAlgorithm())
				.method(request.getMethod())
				.contentType(request.getContentType())
				.date(request.getHeader(HttpHeaders.DATE))
				.nonce(authHeader.getNonce())
				.apiKey(apiKey)
				.apiSecret(apiSecret);

		if (!signatureBuilder.isHashEquals(authHeader.getDigest())) {
			LOGGER.error("Invalid digest");
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid authorization data");
			return;
		}

		filterChain.doFilter(request, response);
	}

	private AuthorizationHeader getAuthHeader(HttpServletRequest request) {

		final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (authHeader == null) {
			return null;
		}

		final Matcher authHeaderMatcher = AUTHORIZATION_HEADER_PATTERN.matcher(authHeader);
		if (!authHeaderMatcher.matches()) {
			return null;
		}

		final String algorithm = authHeaderMatcher.group(1);
		final String apiKey = authHeaderMatcher.group(2);
		final String nonce = authHeaderMatcher.group(3);
		final String receivedDigest = authHeaderMatcher.group(4);

		return new AuthorizationHeader(algorithm, apiKey, nonce, DatatypeConverter.parseBase64Binary(receivedDigest));
	}
}
