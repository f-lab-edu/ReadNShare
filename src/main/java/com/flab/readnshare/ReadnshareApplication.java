package com.flab.readnshare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ReadnshareApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReadnshareApplication.class, args);
	}

}
