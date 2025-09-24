package com.example.carrental;

import com.example.carrental.model.Car;
import com.example.carrental.model.CarType;
import com.example.carrental.repository.CarRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataLoader implements CommandLineRunner {

    private final CarRepository carRepository;

    public DataLoader(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (carRepository.count() == 0) {
            LocalDate today = LocalDate.now();

            carRepository.save(new Car(CarType.SEDAN));
            carRepository.save(new Car(CarType.SUV));
            carRepository.save(new Car(CarType.VAN));
            carRepository.save(new Car(CarType.SEDAN)); // multiple sedans
        }
    }

}
