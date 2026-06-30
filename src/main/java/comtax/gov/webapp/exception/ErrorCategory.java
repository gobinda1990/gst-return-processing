package comtax.gov.webapp.exception;

import lombok.Getter;

/**
 * High-level error categories used across the application.
 *
 * <p>
 * These categories group related {@link ErrorCode}s for logging, monitoring,
 * reporting, and API responses.
 * </p>
 *
 * Example:
 * 
 * <pre>
 * VALIDATION
 * DATABASE
 * SECURITY
 * BUSINESS
 * EXTERNAL_SERVICE
 * SYSTEM
 * </pre>
 */
@Getter
public enum ErrorCategory {

	/**
	 * Request validation errors.
	 */
	VALIDATION("Validation"),

	/**
	 * Database and persistence errors.
	 */
	DATABASE("Database"),

	/**
	 * Authentication and authorization errors.
	 */
	SECURITY("Security"),

	/**
	 * Business rule violations.
	 */
	BUSINESS("Business"),

	/**
	 * External API/service failures.
	 */
	EXTERNAL_SERVICE("External Service"),

	/**
	 * Configuration and environment issues.
	 */
	CONFIGURATION("Configuration"),

	/**
	 * File upload/download/storage errors.
	 */
	FILE("File"),

	/**
	 * System or infrastructure failures.
	 */
	SYSTEM("System"),

	/**
	 * Unknown or uncategorized errors.
	 */
	GENERIC("Generic");

	/**
	 * Human-readable category name.
	 */
	private final String displayName;

	ErrorCategory(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * Returns the display name.
	 */
	@Override
	public String toString() {
		return displayName;
	}
}