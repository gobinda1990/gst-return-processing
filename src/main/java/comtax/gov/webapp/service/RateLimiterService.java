package comtax.gov.webapp.service;

import java.time.Duration;
import org.springframework.stereotype.Service;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;

@Service
public class RateLimiterService {

	private static final int CAPACITY = 100;
	private static final Duration PERIOD = Duration.ofMinutes(1);

	private final LoadingCache<String, Bucket> ipBuckets = Caffeine.newBuilder().expireAfterAccess(Duration.ofHours(1))
			.maximumSize(10_000).build(this::newBucket);

	private Bucket newBucket(String ip) {
		Bandwidth limit = Bandwidth.builder().capacity(CAPACITY).refillIntervally(CAPACITY, PERIOD).build();
		return Bucket.builder().addLimit(limit).build();
	}

	public ConsumptionProbe checkIp(String ip) {
		return ipBuckets.get(ip).tryConsumeAndReturnRemaining(1);
	}

}
