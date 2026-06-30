package comtax.gov.webapp.exception;

import lombok.Getter;

/**
 * Exception thrown for database and persistence related errors.
 *
 * Examples: - Database connection failure - Data integrity violation -
 * Transaction rollback - Query execution failure - Save/Update/Delete failure
 */
@Getter
public class DatabaseException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Application specific error code.
	 */
	private final ErrorCode errorCode;

	/**
	 * Creates DatabaseException using the default message.
	 */
	public DatabaseException() {
		super(ErrorCode.DATABASE_ERROR.getDefaultMessage());
		this.errorCode = ErrorCode.DATABASE_ERROR;
	}

	/**
	 * Creates DatabaseException with default message from ErrorCode.
	 */
	public DatabaseException(ErrorCode errorCode) {
		super(errorCode.getDefaultMessage());
		this.errorCode = errorCode;
	}

	/**
	 * Creates DatabaseException with custom message.
	 */
	public DatabaseException(ErrorCode errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	/**
	 * Creates DatabaseException with custom message and cause.
	 */
	public DatabaseException(ErrorCode errorCode, String message, Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
	}

	/**
	 * Creates DatabaseException with cause.
	 */
	public DatabaseException(ErrorCode errorCode, Throwable cause) {
		super(errorCode.getDefaultMessage(), cause);
		this.errorCode = errorCode;
	}
}