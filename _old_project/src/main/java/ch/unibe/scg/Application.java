package ch.unibe.scg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import ch.unibe.scg.config.MainConfiguration;

/**
 * Main class to start the Spring Boot application. There are two ways to start it. First: Just run the main method of
 * this class. Second: Create a war file with the spring-boot-maven-plugin and deploy it to tomcat or another servlet
 * container.
 *
 * @author Thomas Bosch
 */
@Configuration
@EnableAutoConfiguration(exclude = { BatchAutoConfiguration.class, DataSourceAutoConfiguration.class,
		WebMvcAutoConfiguration.class })
@Import(MainConfiguration.class)
public class Application {

	/**
	 * Run application.
	 * 
	 * @param args
	 *            Parameters to pass to SpringApplication.
	 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}