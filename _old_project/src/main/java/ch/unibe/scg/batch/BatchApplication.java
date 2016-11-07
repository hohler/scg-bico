package ch.unibe.scg.batch;

import org.springframework.boot.SpringApplication;

import ch.unibe.scg.config.BatchConfiguration;

public class BatchApplication {
	public static void main(String [] args) {
		System.setProperty("spring.config.name", "scg-bico-batch");
		System.exit(SpringApplication.exit(SpringApplication.run(
				BatchConfiguration.class, args)));
	}
}