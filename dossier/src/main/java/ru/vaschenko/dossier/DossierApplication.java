package ru.vaschenko.dossier;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@Slf4j
@SpringBootApplication
@EnableFeignClients
public class DossierApplication {
	public static void main(String[] args) {
		log.info("DossierApplication is running on the port 8083");
		SpringApplication.run(DossierApplication.class, args);
	}
}
