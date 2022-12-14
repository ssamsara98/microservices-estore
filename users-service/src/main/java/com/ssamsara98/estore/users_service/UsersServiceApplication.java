package com.ssamsara98.estore.users_service;

import com.ssamsara98.estore.core.config.XStreamConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({XStreamConfig.class})
public class UsersServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(UsersServiceApplication.class, args);
	}
}
