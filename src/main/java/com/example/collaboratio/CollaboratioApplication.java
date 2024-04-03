package com.example.collaboratio;

import com.example.collaboratio.logic.DbConnector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class CollaboratioApplication {

	public static void main(String[] args) {


		SpringApplication.run(CollaboratioApplication.class, args);
	}

}
