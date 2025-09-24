package com.example.carrental;

import com.example.carrental.model.CarType;
import com.example.carrental.service.CarRentalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CarRentalServiceTests {

    private final CarRentalService service;

    CarRentalServiceTests(CarRentalService service) {
        this.service = service;
    }

    @BeforeEach
    void setUp() {
        service.seedData();
    }

    @Test
    void testReservationSuccess() {
        var reservation = service.reserveCar("Sutapa",CarType.SEDAN, LocalDate.now(), 3);
        assertTrue(reservation.isPresent());
    }

    @Test
    void testReservationFailureWhenNoneAvailable() {
        for (int i = 0; i < 5; i++) {
            service.reserveCar("Sutapa", CarType.SEDAN, LocalDate.now(), 3);
        }
        var reservation = service.reserveCar("TestUser", CarType.SEDAN, LocalDate.now(), 3);
        assertFalse(reservation.isPresent());
    }
}
