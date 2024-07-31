package com.example.RepoLister;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class RepoListerApplication {
	public static void main(String[] args) {
		SpringApplication.run(RepoListerApplication.class, args);
	}
	@Bean
	public RestTemplate rest() {
		return new RestTemplate();
	}
}
