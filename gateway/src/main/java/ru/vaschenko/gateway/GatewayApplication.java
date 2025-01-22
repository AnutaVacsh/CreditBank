package ru.vaschenko.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@Slf4j
@SpringBootApplication
@EnableFeignClients
public class GatewayApplication {
	public static void main(String[] args) {
		log.info("GatewayApplication is running on the port 8084");
		SpringApplication.run(GatewayApplication.class, args);
	}

}
