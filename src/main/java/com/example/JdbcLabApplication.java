package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class JdbcLabApplication{

	public static void main(String[] args) {

		if (args.length >= 4) {
			String sqlserverIP = args[0];
			String databasename = args[1];
			String user = args[2];
			String password = args[3];

			System.out.printf("%s, %s, %s, %s", sqlserverIP, databasename, user, password);
		}
		SpringApplication.run(JdbcLabApplication.class, args);
	}

}
