package comtax.gov.webapp.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import comtax.gov.webapp.filter.ActuatorKeyFilter;
import comtax.gov.webapp.filter.ApiKeyFilter;

@Configuration
public class FilterConfig {
	@Bean
	public FilterRegistrationBean<ApiKeyFilter> apiKeyFilterRegistration(ApiKeyFilter apiKeyFilter) {
		FilterRegistrationBean<ApiKeyFilter> reg = new FilterRegistrationBean<>(apiKeyFilter);
		reg.addUrlPatterns("/api/*"); 
		reg.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return reg;
	}

	@Bean
	public FilterRegistrationBean<ActuatorKeyFilter> actuatorFilterRegistration(ActuatorKeyFilter actuatorKeyFilter) {
		FilterRegistrationBean<ActuatorKeyFilter> reg = new FilterRegistrationBean<>(actuatorKeyFilter);
		reg.addUrlPatterns("/actuator/*");
		reg.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
		return reg;
	}

}
