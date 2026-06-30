package comtax.gov.webapp.model;

import java.io.Serializable;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Authentication Response
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * API Status SUCCESS / FAILED
	 */
	private String status;

	/**
	 * Status Code Example : 1 = Success
	 */
	private String statusCd;

	/**
	 * Response Message
	 */
	private String message;

	/**
	 * JWT Access Token
	 */
	private String authToken;

	/**
	 * JWT Refresh Token
	 */
	private String refreshToken;

	/**
	 * Encrypted Session Encryption Key (SEK)
	 */
	private String sek;

	/**
	 * JWT Token Type
	 */
	@Builder.Default
	private String tokenType = "Bearer";

	/**
	 * Token validity in seconds
	 */
	private Long expiresIn;

	/**
	 * Token generation time
	 */
	private Instant issuedAt;

	/**
	 * Token expiry time
	 */
	private Instant expiresAt;

	/**
	 * Authenticated Client Id
	 */
	private String clientId;

	/**
	 * Authenticated User
	 */
	private String username;

	/**
	 * GST State Code
	 */
	private String stateCd;

}
