package comtax.gov.webapp.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Centralized application error codes.
 *
 * Error Code Ranges: 1xxx - Validation / Request 2xxx - Database 3xxx -
 * Security 4xxx - Business / Resource 5xxx - External Services 6xxx -
 * Configuration 7xxx - File Operations 8xxx - System 9xxx - Generic
 */
@Getter
public enum ErrorCode {

	// ==========================================================
	// 1xxx - Validation / Request
	// ==========================================================

	VALIDATION_ERROR("ERR-1001", "Validation failed", HttpStatus.BAD_REQUEST, true),
	INVALID_REQUEST("ERR-1002", "Invalid request", HttpStatus.BAD_REQUEST, true),
	INVALID_REQUEST_BODY("ERR-1003", "Invalid request body", HttpStatus.BAD_REQUEST, true),
	MALFORMED_JSON("ERR-1004", "Malformed JSON request", HttpStatus.BAD_REQUEST, true),
	MISSING_PARAMETER("ERR-1005", "Missing required parameter", HttpStatus.BAD_REQUEST, true),
	INVALID_PARAMETER("ERR-1006", "Invalid parameter", HttpStatus.BAD_REQUEST, true),
	INVALID_PATH_PARAMETER("ERR-1007", "Invalid path parameter", HttpStatus.BAD_REQUEST, true),
	INVALID_QUERY_PARAMETER("ERR-1008", "Invalid query parameter", HttpStatus.BAD_REQUEST, true),
	UNSUPPORTED_MEDIA_TYPE("ERR-1009", "Unsupported media type", HttpStatus.UNSUPPORTED_MEDIA_TYPE, true),
	METHOD_NOT_ALLOWED("ERR-1010", "HTTP method not allowed", HttpStatus.METHOD_NOT_ALLOWED, true),

	// ==========================================================
	// 2xxx - Database
	// ==========================================================

	DATABASE_ERROR("ERR-2001", "Database error", HttpStatus.INTERNAL_SERVER_ERROR, false),
	DATABASE_CONNECTION_FAILED("ERR-2002", "Database connection failed", HttpStatus.INTERNAL_SERVER_ERROR, false),
	DATABASE_TIMEOUT("ERR-2003", "Database timeout", HttpStatus.GATEWAY_TIMEOUT, false),
	DATA_INTEGRITY_VIOLATION("ERR-2004", "Data integrity violation", HttpStatus.CONFLICT, false),
	TRANSACTION_FAILED("ERR-2005", "Transaction failed", HttpStatus.INTERNAL_SERVER_ERROR, false),

	// ==========================================================
	// 3xxx - Security
	// ==========================================================

	UNAUTHORIZED("ERR-3001", "Authentication required", HttpStatus.UNAUTHORIZED, true),
	INVALID_TOKEN("ERR-3002", "Invalid token", HttpStatus.UNAUTHORIZED, true),
	TOKEN_EXPIRED("ERR-3003", "Token expired", HttpStatus.UNAUTHORIZED, true),
	ACCESS_DENIED("ERR-3004", "Access denied", HttpStatus.FORBIDDEN, true),
	CSRF_ERROR("ERR-3005", "CSRF validation failed", HttpStatus.FORBIDDEN, true),
	RATE_LIMIT_EXCEEDED("ERR-3006", "Too many requests. Please try again later.",HttpStatus.TOO_MANY_REQUESTS,true),

	// ==========================================================
	// 4xxx - Business
	// ==========================================================

	RESOURCE_NOT_FOUND("ERR-4001", "Resource not found", HttpStatus.NOT_FOUND, true),
	RESOURCE_ALREADY_EXISTS("ERR-4002", "Resource already exists", HttpStatus.CONFLICT, true),
	DUPLICATE_RESOURCE("ERR-4003", "Duplicate resource", HttpStatus.CONFLICT, true),
	BUSINESS_RULE_VIOLATION("ERR-4004", "Business rule violation", HttpStatus.UNPROCESSABLE_ENTITY, true),
	OPERATION_NOT_ALLOWED("ERR-4005", "Operation not allowed", HttpStatus.UNPROCESSABLE_ENTITY, true),

	// ==========================================================
	// 5xxx - External Services
	// ==========================================================

	SERVICE_UNAVAILABLE("ERR-5001", "Service unavailable", HttpStatus.SERVICE_UNAVAILABLE, false),
	EXTERNAL_SERVICE_ERROR("ERR-5002", "External service error", HttpStatus.BAD_GATEWAY, false),
	EXTERNAL_SERVICE_TIMEOUT("ERR-5003", "External service timeout", HttpStatus.GATEWAY_TIMEOUT, false),

	// ==========================================================
	// 6xxx - Configuration
	// ==========================================================

	CONFIGURATION_ERROR("ERR-6001", "Configuration error", HttpStatus.INTERNAL_SERVER_ERROR, false),
	MISSING_CONFIGURATION("ERR-6002", "Missing configuration", HttpStatus.INTERNAL_SERVER_ERROR, false),

	// ==========================================================
	// 7xxx - File
	// ==========================================================

	FILE_NOT_FOUND("ERR-7001", "File not found", HttpStatus.NOT_FOUND, true),
	FILE_UPLOAD_FAILED("ERR-7002", "File upload failed", HttpStatus.INTERNAL_SERVER_ERROR, false),
	FILE_DOWNLOAD_FAILED("ERR-7003", "File download failed", HttpStatus.INTERNAL_SERVER_ERROR, false),
	INVALID_FILE_TYPE("ERR-7004", "Invalid file type", HttpStatus.BAD_REQUEST, true),
	FILE_SIZE_EXCEEDED("ERR-7005", "File size exceeded", HttpStatus.PAYLOAD_TOO_LARGE, true),

	// ==========================================================
	// 8xxx - System
	// ==========================================================

	INTERNAL_SERVER_ERROR("ERR-8001", "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR, false),
	SERIALIZATION_ERROR("ERR-8002", "Serialization error", HttpStatus.INTERNAL_SERVER_ERROR, false),
	MEMORY_EXHAUSTED("ERR-8003", "Memory exhausted", HttpStatus.INTERNAL_SERVER_ERROR, false),
	SYSTEM_MAINTENANCE("ERR-8004", "System under maintenance", HttpStatus.SERVICE_UNAVAILABLE, false),

	// ==========================================================
	// 9xxx - Generic
	// ==========================================================

	UNKNOWN_ERROR("ERR-9001", "Unknown error", HttpStatus.INTERNAL_SERVER_ERROR, false),
	NOT_IMPLEMENTED("ERR-9002", "Feature not implemented", HttpStatus.NOT_IMPLEMENTED, true);

	private final String code;
	private final String defaultMessage;
	private final HttpStatus httpStatus;
	private final boolean clientError;

	ErrorCode(String code, String defaultMessage, HttpStatus httpStatus, boolean clientError) {

		this.code = code;
		this.defaultMessage = defaultMessage;
		this.httpStatus = httpStatus;
		this.clientError = clientError;
	}

	public boolean isServerError() {
		return !clientError;
	}

	// ==========================================================
	// Fast Lookup
	// ==========================================================

	private static final Map<String, ErrorCode> CODE_MAP = Arrays.stream(values())
			.collect(Collectors.toMap(ErrorCode::getCode, Function.identity()));

	public static ErrorCode fromCode(String code) {
		return CODE_MAP.getOrDefault(code, UNKNOWN_ERROR);
	}

	private static final Map<HttpStatus, ErrorCode> STATUS_MAP = Arrays.stream(values())
			.collect(Collectors.toMap(ErrorCode::getHttpStatus, Function.identity(), (first, second) -> first));

	public static ErrorCode fromStatus(HttpStatus status) {
		return STATUS_MAP.getOrDefault(status, UNKNOWN_ERROR);
	}

	@Override
	public String toString() {
		return code + " - " + defaultMessage;
	}
}