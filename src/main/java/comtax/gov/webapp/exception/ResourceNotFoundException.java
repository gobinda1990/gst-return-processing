package comtax.gov.webapp.exception;

import lombok.Getter;

/**
 * Exception thrown when a requested resource is not found.
 */
@Getter
public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final ErrorCode errorCode;

	/**
	 * Creates an exception with the default message.
	 */
	public ResourceNotFoundException() {
		super(ErrorCode.RESOURCE_NOT_FOUND.getDefaultMessage());
		this.errorCode = ErrorCode.RESOURCE_NOT_FOUND;
	}

	/**
	 * Creates an exception with a custom message.
	 *
	 * @param message custom error message
	 */
	public ResourceNotFoundException(String message) {
		super(message);
		this.errorCode = ErrorCode.RESOURCE_NOT_FOUND;
	}

	/**
	 * Creates an exception with a custom message and cause.
	 *
	 * @param message custom error message
	 * @param cause   root cause
	 */
	public ResourceNotFoundException(String message, Throwable cause) {
		super(message, cause);
		this.errorCode = ErrorCode.RESOURCE_NOT_FOUND;
	}

	/**
	 * Creates an exception with a specific ErrorCode.
	 *
	 * @param errorCode application error code
	 * @param message   custom error message
	 */
	public ResourceNotFoundException(ErrorCode errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}
}