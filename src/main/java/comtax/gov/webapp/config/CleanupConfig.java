package comtax.gov.webapp.config;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;

@Configuration
@Slf4j
public class CleanupConfig {

	@PreDestroy
	public void onShutdown() {
		log.info("[CleanupConfig] Running shutdown hooks...");

		// JDBC driver deregistration
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		Enumeration<Driver> drivers = DriverManager.getDrivers();
		while (drivers.hasMoreElements()) {
			Driver driver = drivers.nextElement();
			if (driver.getClass().getClassLoader() == cl) {
				try {
					log.info("Deregistering JDBC driver: " + driver);
					DriverManager.deregisterDriver(driver);
				} catch (SQLException e) {
					log.error("Error deregistering driver: " + driver);
					e.printStackTrace();
				}
			}
		}

		// Shut down loggers
		try {
			System.out.println("Shutting down Log4j2...");
			LogManager.shutdown();
		} catch (Exception e) {
			System.err.println("Error shutting down Log4j2.");
			e.printStackTrace();
		}

		// Flush Java introspector caches (optional)
		java.beans.Introspector.flushCaches();
	}

}
