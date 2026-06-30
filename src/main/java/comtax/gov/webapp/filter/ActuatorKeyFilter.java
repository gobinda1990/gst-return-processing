package comtax.gov.webapp.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import comtax.gov.webapp.exception.ErrorCode;
import comtax.gov.webapp.model.ApiResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
@RequiredArgsConstructor
public class ActuatorKeyFilter extends OncePerRequestFilter {

	private static final String HEADER_ACTUATOR_KEY = "X-ACTUATOR-KEY";
	private static final String ACTUATOR_PATH_PREFIX = "/ChatBot/actuator/";

	@Value("${actuator.security.key}")
	private String expectedActuatorKey;

	private final ObjectMapper objectMapper;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String suppliedKey = request.getHeader(HEADER_ACTUATOR_KEY);
		if (!isValidKey(suppliedKey)) {
			log.warn("Unauthorized Actuator access to {} with key: {}", request.getRequestURI(), suppliedKey);
			writeError(response, ErrorCode.UNAUTHORIZED, "Unauthorized - Invalid API Key");
			return;
		}

		filterChain.doFilter(request, response);
	}

	private boolean isValidKey(String suppliedKey) {
		return expectedActuatorKey != null && expectedActuatorKey.equals(suppliedKey);
	}

	private void writeError(HttpServletResponse response, ErrorCode errorCode, String message) throws IOException {

		if (response.isCommitted()) {
			return;
		}

		response.resetBuffer();
		response.setStatus(errorCode.getHttpStatus().value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());

		response.setHeader(HttpHeaders.CACHE_CONTROL, "no-store");
		response.setHeader(HttpHeaders.PRAGMA, "no-cache");

		ApiResponse<Void> body = ApiResponse.error(errorCode, message);

		objectMapper.writeValue(response.getWriter(), body);
	}

	/**
	 * Skip non-actuator paths and pre-flight OPTIONS.
	 */
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		String uri = request.getRequestURI();
		boolean skip = !uri.startsWith(ACTUATOR_PATH_PREFIX) || HttpMethod.OPTIONS.matches(request.getMethod());
		log.debug("ActuatorKeyFilter.shouldNotFilter URI [{}] => {}", uri, skip);
		return skip;
	}
}
