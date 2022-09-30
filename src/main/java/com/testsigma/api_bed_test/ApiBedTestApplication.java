package com.testsigma.api_bed_test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication(scanBasePackages = "com.testsigma.api_bed_test")
@Configuration
@EnableWebMvc
@EnableJpaAuditing
@EnableResourceServer
public class ApiBedTestApplication {

	public static void main(String[] args) {
		try {
			SpringApplication application = new SpringApplication(ApiBedTestApplication.class);
			application.addListeners(new ApplicationPidFileWriter("./bin/app.pid"));
			application.run(args);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

}
