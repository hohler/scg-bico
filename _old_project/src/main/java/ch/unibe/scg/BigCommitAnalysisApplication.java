package ch.unibe.scg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class BigCommitAnalysisApplication {

	public static void main(String[] args) {
		System.setProperty("spring.config.name", "scg-bico");
		//SpringApplication.run(BigCommitAnalysisApplication.class, args);
		
		SpringApplicationBuilder b = new SpringApplicationBuilder(BigCommitAnalysisApplication.class);
		b.run(args);
		
	}
}
