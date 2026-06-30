package comtax.gov.webapp.exception;

import lombok.Getter;

/**
 * Exception thrown for business rule violations.
 *
 * Examples: - Duplicate record - Invalid business state - Resource already
 * exists - Insufficient balance - Invalid workflow transition
 */
@Getter
public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Application specific error code.
	 */
	private final ErrorCode errorCode;

	/**
	 * Creates a BusinessException using the default message from the ErrorCode.
	 *
	 * @param errorCode application error code
	 */
	public BusinessException(ErrorCode errorCode) {
		super(errorCode.getDefaultMessage());
		this.errorCode = errorCode;
	}

	/**
	 * Creates a BusinessException with a custom message.
	 *
	 * @param errorCode application error code
	 * @param message   custom error message
	 */
	public BusinessException(ErrorCode errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	/**
	 * Creates a BusinessException with a custom message and cause.
	 *
	 * @param errorCode application error code
	 * @param message   custom error message
	 * @param cause     root cause
	 */
	public BusinessException(ErrorCode errorCode, String message, Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
	}
}