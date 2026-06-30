package comtax.gov.webapp.model;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * Validation Error Details.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiError implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * Field Name.
	 */
	private String field;

	/**
	 * Rejected Value.
	 */
	private Object rejectedValue;

	/**
	 * Validation Message.
	 */
	private String message;
}