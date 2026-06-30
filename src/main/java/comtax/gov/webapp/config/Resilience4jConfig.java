package comtax.gov.webapp.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;

@Configuration
public class Resilience4jConfig {

	@Bean
	public RateLimiterRegistry rateLimiterRegistry() {
		RateLimiterConfig config = RateLimiterConfig.custom().limitForPeriod(100)
				.limitRefreshPeriod(Duration.ofMinutes(1)).timeoutDuration(Duration.ZERO).build();
		return RateLimiterRegistry.of(config);
	}

}
