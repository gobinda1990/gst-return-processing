package comtax.gov.webapp.config;

import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@OpenAPIDefinition(
	    info = @Info(
	            title = "ChatBot API",
	            version = "1.0.0",
	            description = "Sample Swagger/OpenAPI documentation for Spring Boot 3.5"
	        )
	    )
public class OpenApiConfig {

}
