package com.redixExample.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@Configuration
@EnableTransactionManagement
@RestController
@EnableCaching
@ImportResource("classpath:appCtx.xml")  
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
