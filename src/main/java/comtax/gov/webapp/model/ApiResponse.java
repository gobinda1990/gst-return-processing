package comtax.gov.webapp.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import comtax.gov.webapp.exception.ErrorCode;
import lombok.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Standard API Response Wrapper.
 *
 * <p>Used for all REST API responses including:
 * <ul>
 *     <li>Success Responses</li>
 *     <li>Validation Errors</li>
 *     <li>Business Errors</li>
 *     <li>System Errors</li>
 * </ul>
 *
 * @param <T> Response Payload
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * HTTP Status Code.
     */
    private int status;

    /**
     * Indicates request success.
     */
    private boolean success;

    /**
     * Response message.
     */
    private String message;

    /**
     * Application Error Code.
     */
    private ErrorCode errorCode;

    /**
     * Actual Response Data.
     */
    private T data;

    /**
     * Validation / Business Errors.
     */
    private List<ApiError> errors;

    /**
     * Unique Request Identifier.
     */
    private String requestId;

    /**
     * Request URI.
     */
    private String path;

    /**
     * API Version.
     */
    private String version;

    /**
     * Response Timestamp.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    // ==========================================================
    // Success Responses
    // ==========================================================

    public static <T> ApiResponse<T> success(T data) {

        return success("Request processed successfully.", data);
    }

    public static <T> ApiResponse<T> success(String message, T data) {

        return ApiResponse.<T>builder()
                .status(200)
                .success(true)
                .message(message)
                .data(data)
                .version("v1")
                .build();
    }

    public static <T> ApiResponse<T> success(String message) {

        return ApiResponse.<T>builder()
                .status(200)
                .success(true)
                .message(message)
                .version("v1")
                .build();
    }

    // ==========================================================
    // Error Responses
    // ==========================================================

    public static <T> ApiResponse<T> error(ErrorCode errorCode) {

        return error(errorCode,
                errorCode.getDefaultMessage(),
                null);
    }

    public static <T> ApiResponse<T> error(
            ErrorCode errorCode,
            String message) {

        return error(errorCode,
                message,
                null);
    }

    public static <T> ApiResponse<T> error(
            ErrorCode errorCode,
            List<ApiError> errors) {

        return error(errorCode,
                errorCode.getDefaultMessage(),
                errors);
    }

    public static <T> ApiResponse<T> error(
            ErrorCode errorCode,
            String message,
            List<ApiError> errors) {

        return ApiResponse.<T>builder()
                .status(errorCode.getHttpStatus().value())
                .success(false)
                .message(message)
                .errorCode(errorCode)
                .errors(errors)
                .version("v1")
                .build();
    }

}