package comtax.gov.webapp.exception;

import comtax.gov.webapp.model.ApiError;
import comtax.gov.webapp.model.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import jakarta.validation.ConstraintViolationException;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

//	@ExceptionHandler(BadCredentialsException.class)
//	public ResponseEntity<ApiResponse<Void>> handleBadCredentials(BadCredentialsException ex,
//			HttpServletRequest request) {
//
//		log.warn("Authentication failed: {}", ex.getMessage());
//
//		return buildResponse(ErrorCode.UNAUTHORIZED, "Invalid username or password.", null, request);
//	}
//
//	@ExceptionHandler(UsernameNotFoundException.class)
//	public ResponseEntity<ApiResponse<Void>> handleUsernameNotFound(UsernameNotFoundException ex,
//			HttpServletRequest request) {
//
//		log.warn("{}", ex.getMessage());
//
//		return buildResponse(ErrorCode.UNAUTHORIZED, "Invalid username or password.", null, request);
//	}
//
//	@ExceptionHandler(LockedException.class)
//	public ResponseEntity<ApiResponse<Void>> handleLocked(LockedException ex, HttpServletRequest request) {
//
//		return buildResponse(ErrorCode.UNAUTHORIZED, "User account is locked.", null, request);
//	}
//
//	@ExceptionHandler(AccountExpiredException.class)
//	public ResponseEntity<ApiResponse<Void>> handleAccountExpired(AccountExpiredException ex,
//			HttpServletRequest request) {
//
//		return buildResponse(ErrorCode.UNAUTHORIZED, "User account has expired.", null, request);
//	}
//
//	@ExceptionHandler(CredentialsExpiredException.class)
//	public ResponseEntity<ApiResponse<Void>> handleCredentialsExpired(CredentialsExpiredException ex,
//			HttpServletRequest request) {
//
//		return buildResponse(ErrorCode.UNAUTHORIZED, "Password has expired.", null, request);
//	}
//
//	@ExceptionHandler(AccessDeniedException.class)
//	public ResponseEntity<ApiResponse<Void>> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
//
//		return buildResponse(ErrorCode.ACCESS_DENIED, "Access denied.", null, request);
//	}

	// ==========================================================
	// Validation Error
	// ==========================================================

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException ex,
			HttpServletRequest request) {

		List<ApiError> errors = ex.getBindingResult().getFieldErrors().stream().map(this::buildFieldError).toList();

		log.warn("Validation failed : {}", errors);

		return buildResponse(ErrorCode.VALIDATION_ERROR, "Validation failed.", errors, request);
	}

	// ==========================================================
	// Constraint Validation
	// ==========================================================

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ApiResponse<Void>> handleConstraintViolation(ConstraintViolationException ex,
			HttpServletRequest request) {

		List<ApiError> errors = ex.getConstraintViolations().stream()
				.map(v -> ApiError.builder().field(v.getPropertyPath().toString()).rejectedValue(v.getInvalidValue())
						.message(v.getMessage()).build())
				.toList();

		return buildResponse(ErrorCode.VALIDATION_ERROR, "Validation failed.", errors, request);
	}

	// ==========================================================
	// Illegal Argument
	// ==========================================================

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ApiResponse<Void>> handleIllegalArgument(IllegalArgumentException ex,
			HttpServletRequest request) {

		log.warn(ex.getMessage());

		return buildResponse(ErrorCode.INVALID_PARAMETER, ex.getMessage(), null, request);
	}

	// ==========================================================
	// Resource Not Found
	// ==========================================================

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponse<Void>> handleResourceNotFound(ResourceNotFoundException ex,
			HttpServletRequest request) {

		log.warn(ex.getMessage());

		return buildResponse(ErrorCode.RESOURCE_NOT_FOUND, ex.getMessage(), null, request);
	}

	// ==========================================================
	// Business Exception
	// ==========================================================

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException ex, HttpServletRequest request) {

		log.warn(ex.getMessage());

		return buildResponse(ex.getErrorCode(), ex.getMessage(), null, request);
	}

	// ==========================================================
	// Database
	// ==========================================================

	@ExceptionHandler({ DatabaseException.class, DataAccessException.class })
	public ResponseEntity<ApiResponse<Void>> handleDatabase(Exception ex, HttpServletRequest request) {

		log.error("Database error", ex);

		return buildResponse(ErrorCode.DATABASE_ERROR, null, null, request);
	}

	// ==========================================================
	// JSON Parse
	// ==========================================================

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ApiResponse<Void>> handleJson(HttpMessageNotReadableException ex,
			HttpServletRequest request) {

		return buildResponse(ErrorCode.MALFORMED_JSON, null, null, request);
	}

	// ==========================================================
	// Missing Parameter
	// ==========================================================

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<ApiResponse<Void>> handleMissingParameter(MissingServletRequestParameterException ex,
			HttpServletRequest request) {

		return buildResponse(ErrorCode.MISSING_PARAMETER, ex.getParameterName() + " is required.", null, request);
	}

	// ==========================================================
	// Missing Header
	// ==========================================================

	@ExceptionHandler(MissingRequestHeaderException.class)
	public ResponseEntity<ApiResponse<Void>> handleMissingHeader(MissingRequestHeaderException ex,
			HttpServletRequest request) {

		return buildResponse(ErrorCode.MISSING_PARAMETER, ex.getHeaderName() + " header is required.", null, request);
	}

	// ==========================================================
	// Method Not Allowed
	// ==========================================================

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ApiResponse<Void>> handleMethod(HttpRequestMethodNotSupportedException ex,
			HttpServletRequest request) {

		return buildResponse(ErrorCode.METHOD_NOT_ALLOWED, null, null, request);
	}

	// ==========================================================
	// Media Type
	// ==========================================================

	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public ResponseEntity<ApiResponse<Void>> handleMediaType(HttpMediaTypeNotSupportedException ex,
			HttpServletRequest request) {

		return buildResponse(ErrorCode.UNSUPPORTED_MEDIA_TYPE, null, null, request);
	}

	// ==========================================================
	// URL Not Found
	// ==========================================================

	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<ApiResponse<Void>> handleNoHandler(NoHandlerFoundException ex, HttpServletRequest request) {

		return buildResponse(ErrorCode.RESOURCE_NOT_FOUND, "Requested URL not found.", null, request);
	}

	// ==========================================================
	// Catch All
	// ==========================================================

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<Void>> handleException(Exception ex, HttpServletRequest request) {

		log.error("Unhandled Exception", ex);

		return buildResponse(ErrorCode.INTERNAL_SERVER_ERROR, null, null, request);
	}

	// ==========================================================
	// Common Response Builder
	// ==========================================================

	private ResponseEntity<ApiResponse<Void>> buildResponse(ErrorCode errorCode, String message, List<ApiError> errors,
			HttpServletRequest request) {

		ApiResponse<Void> response = ApiResponse.<Void>builder().status(errorCode.getHttpStatus().value())
				.success(false).message(message == null ? errorCode.getDefaultMessage() : message).errorCode(errorCode)
				.errors(errors).requestId(MDC.get("requestId")).path(request.getRequestURI()).version("v1").build();

		return ResponseEntity.status(HttpStatusCode.valueOf(errorCode.getHttpStatus().value())).body(response);
	}

	private ApiError buildFieldError(FieldError fieldError) {

		return ApiError.builder().field(fieldError.getField()).rejectedValue(fieldError.getRejectedValue())
				.message(fieldError.getDefaultMessage()).build();
	}

}