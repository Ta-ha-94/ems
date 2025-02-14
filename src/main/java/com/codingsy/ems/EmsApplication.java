package com.codingsy.ems;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.autoconfigure.domain.EntityScan;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = PageSerializationMode.VIA_DTO)
//@EntityScan(value = "com.codingsy.ems.model") //Use when model classes are not defined inside the base-package (main application class)
//@EnableJpaRepositories(value = "com.codingsy.ems.repositoru") //Use when JPS repositories are not defined inside the base-package (main application class)
//@EnableWebSecurity //Use to enable the spring security feature inside a Spring application. SpringBoot is smart enough to enable this configuration based on the dependencies in pom.xml 
public class EmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmsApplication.class, args);
	}
}
