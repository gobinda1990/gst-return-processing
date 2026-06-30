package comtax.gov.webapp.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import comtax.gov.webapp.exception.ErrorCode;
import comtax.gov.webapp.model.ApiResponse;
import comtax.gov.webapp.service.IpLogService;
import comtax.gov.webapp.service.RateLimiterService;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
public class ApiKeyFilter extends OncePerRequestFilter {

	private static final String HEADER_API_KEY = "X-API-KEY";

	private static final String HEADER_RATE_LIMIT = "X-RateLimit-Limit";
	private static final String HEADER_RATE_REMAINING = "X-RateLimit-Remaining";
	private static final String HEADER_RATE_RESET = "X-RateLimit-Reset";

	private static final Set<String> SENSITIVE_PARAMS = Set.of("password", "passwd", "otp", "token", "access_token",
			"refresh_token", "apiKey", "secret");

	@Value("${api.security.key}")
	private String expectedApiKey;

	@Value("${api.prefix:/api/v1/gst-return-3b}")
	private String apiPrefix;

	@Value("${api.rate.limit:100}")
	private int maxRequests;

	private final RateLimiterService rateLimiterService;
	private final IpLogService ipLogService;
	private final ObjectMapper objectMapper;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		final String requestUri = request.getRequestURI();
		final String requestParams = buildRequestParams(request);
		final String clientIp = resolveClientIp(request);

		log.info("Request URI={} Params={} IP={}", requestUri, requestParams, clientIp);

		if (!isValidApiKey(request.getHeader(HEADER_API_KEY))) {

			log.warn("Unauthorized request. URI={} IP={}", requestUri, clientIp);

			 ipLogService.logIp(clientIp, requestUri, requestParams, "FAIL");

			writeError(response, ErrorCode.UNAUTHORIZED, "Unauthorized - Invalid API Key");

			return;
		}

		try {

			ConsumptionProbe probe = rateLimiterService.checkIp(clientIp);

			if (!probe.isConsumed()) {

				setRetryAfterHeader(response, probe);

				 ipLogService.logIp(clientIp, requestUri, requestParams, "RATE_LIMIT");

				writeError(response, ErrorCode.RATE_LIMIT_EXCEEDED, "Too many requests");

				return;
			}

			addSecurityHeaders(response);
			setRateLimitHeaders(response, probe);

			 ipLogService.logIp(clientIp, requestUri, requestParams, "SUCCESS");

			filterChain.doFilter(request, response);

		} catch (Exception ex) {

			log.error("Error while processing request from IP={}", clientIp, ex);

			 ipLogService.logIp(clientIp, requestUri, requestParams, "ERROR");

			writeError(response,ErrorCode.INTERNAL_SERVER_ERROR,"Internal Server Error");
		}
	}

	/**
	 * Constant-time API key comparison.
	 */
	private boolean isValidApiKey(String apiKey) {

		if (expectedApiKey == null || apiKey == null) {
			return false;
		}

		return MessageDigest.isEqual(expectedApiKey.getBytes(StandardCharsets.UTF_8),
				apiKey.getBytes(StandardCharsets.UTF_8));
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

		ApiResponse<Void> apiResponse = ApiResponse.error(errorCode, message);

		objectMapper.writeValue(response.getWriter(), apiResponse);
	}

	private void addSecurityHeaders(HttpServletResponse response) {

		response.setHeader("X-Content-Type-Options", "nosniff");
		response.setHeader("X-Frame-Options", "DENY");
		response.setHeader("Referrer-Policy", "no-referrer");
	}

	private void setRetryAfterHeader(HttpServletResponse response, ConsumptionProbe probe) {

		long retryAfter = probe.getNanosToWaitForRefill() / 1_000_000_000L;

		response.setHeader(HttpHeaders.RETRY_AFTER, String.valueOf(retryAfter));
	}

	private void setRateLimitHeaders(HttpServletResponse response, ConsumptionProbe probe) {

		response.setHeader(HEADER_RATE_LIMIT, String.valueOf(maxRequests));

		response.setHeader(HEADER_RATE_REMAINING, String.valueOf(probe.getRemainingTokens()));

		response.setHeader(HEADER_RATE_RESET,
				String.valueOf(Instant.now().plusNanos(probe.getNanosToWaitForRefill()).getEpochSecond()));
	}

	private String buildRequestParams(HttpServletRequest request) {

		return request.getParameterMap().entrySet().stream()
				.filter(entry -> !SENSITIVE_PARAMS.contains(entry.getKey().toLowerCase()))
				.map(entry -> entry.getKey() + "=" + Arrays.toString(entry.getValue()))
				.collect(Collectors.joining("&"));
	}

	/**
	 * If your application is behind a trusted reverse proxy (Nginx, Apache, Load
	 * Balancer), configure Spring's ForwardedHeaderFilter and use the forwarded
	 * header. Otherwise, request.getRemoteAddr() is safer.
	 */
	private String resolveClientIp(HttpServletRequest request) {

		String forwarded = request.getHeader("X-Forwarded-For");

		if (forwarded != null && !forwarded.isBlank()) {
			return forwarded.split(",")[0].trim();
		}

		return request.getRemoteAddr();
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {

		return !request.getRequestURI().startsWith(apiPrefix) || HttpMethod.OPTIONS.matches(request.getMethod());
	}
}