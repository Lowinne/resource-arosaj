package com.epsi.arosaj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories("com.epsi..arosaj.persistence.repository")
@EntityScan("com.epsi.arosaj.persistence.model")
@SpringBootApplication
public class ArosajApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArosajApplication.class, args);
	}

}
