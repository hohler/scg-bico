package ch.unibe.scg.batch;

import org.springframework.boot.SpringApplication;

public class BatchApplication {
	public static void main(String [] args) {
		System.exit(SpringApplication.exit(SpringApplication.run(
				BatchConfiguration.class, args)));
	}
}