package com.bob.projects.twoFactorAuth;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
@ComponentScan({
		"com.bob.projects.twoFactorAuth.*",
})
@SpringBootApplication
public class TwoFactorAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(TwoFactorAuthApplication.class, args);
	}
}
