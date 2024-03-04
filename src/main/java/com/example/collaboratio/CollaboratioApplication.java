package com.example.collaboratio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.jdbc.config.annotation.SpringSessionDataSource;


@SpringBootApplication


public class CollaboratioApplication {

	public static void main(String[] args) {
		SpringApplication.run(CollaboratioApplication.class, args);
	}

}
