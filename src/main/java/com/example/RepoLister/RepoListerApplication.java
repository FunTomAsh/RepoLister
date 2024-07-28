package com.example.RepoLister;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RepoListerApplication {

	//private static final Logger log = LoggerFactory.getLogger(RepoListerApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(RepoListerApplication.class, args);
		//log.info("Application started successfully");
	}

/*	@Bean
	CommandLineRunner runner(){
		return args -> {
			UsersRepos ur = new UsersRepos("TomashKarpei", "RepoLister", Collections.singletonList(new Branch("B1", "lastComm")));
            log.info("Runned: " + ur);
		};
	}*/
}
