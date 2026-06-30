package comtax.gov.webapp.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest {

    @NotBlank(message = "Client Id is required")
    @Size(min = 3, max = 100)
    private String clientId;

    @NotBlank(message = "Client Secret is required")
    @Size(min = 8, max = 200)
    private String clientSecret;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 100)
    private String username;

    /**
     * RSA Encrypted Password (Base64)
     */
    @NotBlank(message = "Password is required")
    private String password;

    /**
     * GST State Code
     * Example: 29
     */
    @NotBlank(message = "State Code is required")
    @Pattern(regexp = "^[0-9]{2}$", message = "Invalid State Code")
    private String stateCd;

    /**
     * RSA Encrypted App Key (Base64)
     */
    @NotBlank(message = "App Key is required")
    private String appKey;

}
