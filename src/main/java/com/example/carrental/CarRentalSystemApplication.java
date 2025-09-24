package com.example.carrental;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CarRentalSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(CarRentalSystemApplication.class, args);
    }
}

