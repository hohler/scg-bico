package ch.unibe.scg.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * This is the main configuration class for the application.
 */
@Configuration
@Import({ ServletConfiguration.class, WebappConfiguration.class })
public class MainConfiguration {

}