package ru.vaschenko.deal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@Slf4j
@SpringBootApplication
@EnableFeignClients
public class DealApplication {

  public static void main(String[] args) {
    log.info("DealApplication is running on the port 8081");
    SpringApplication.run(DealApplication.class, args);
  }
}
