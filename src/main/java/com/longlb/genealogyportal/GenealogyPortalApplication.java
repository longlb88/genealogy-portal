package com.longlb.genealogyportal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class GenealogyPortalApplication {

	public static void main(String[] args) {
		SpringApplication.run(GenealogyPortalApplication.class, args);
	}

}
