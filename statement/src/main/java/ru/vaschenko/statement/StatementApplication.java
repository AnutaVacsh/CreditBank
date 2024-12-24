package ru.vaschenko.statement;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@Slf4j
@SpringBootApplication
@EnableFeignClients
public class StatementApplication {
	public static void main(String[] args) {
		log.info("StatementApplication is running on the port 8082");
		SpringApplication.run(StatementApplication.class, args);
	}

}
