package com.example.carrental;

import com.example.carrental.model.CarType;
import com.example.carrental.service.CarRentalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CarRentalServiceTests {

    @Autowired
    private CarRentalService service;

    private LocalDateTime start;

    @BeforeEach
    void setUp() {
        start = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0);
    }

    @Test
    void testSedanReservationsRespectInventory() {
        // First 2 reservations succeed
        assertTrue(service.reserveCar("User1", CarType.SEDAN, start, 3).isPresent());
        assertTrue(service.reserveCar("User2", CarType.SEDAN, start, 3).isPresent());

        // Third reservation fails
        assertFalse(service.reserveCar("User3", CarType.SEDAN, start, 3).isPresent());
    }

    @Test
    void testSUVReservation() {
        // Only 1 SUV available
        assertTrue(service.reserveCar("UserA", CarType.SUV, start, 2).isPresent());
        assertFalse(service.reserveCar("UserB", CarType.SUV, start, 2).isPresent());
    }

    @Test
    void testVanReservation() {

        // Only 1 Van available
        assertTrue(service.reserveCar("UserX", CarType.VAN, start, 1).isPresent());
        assertFalse(service.reserveCar("UserY", CarType.VAN, start, 1).isPresent());
    }

    @Test
    void testMultipleUsersDifferentTypes() {
        // Reserve all cars simultaneously
        assertTrue(service.reserveCar("Alice", CarType.SEDAN, start, 2).isPresent());
        assertTrue(service.reserveCar("Bob", CarType.SEDAN, start, 2).isPresent());
        assertTrue(service.reserveCar("Charlie", CarType.SUV, start, 2).isPresent());
        assertTrue(service.reserveCar("Diana", CarType.VAN, start, 2).isPresent());

        // No more cars should be available
        assertFalse(service.reserveCar("Eve", CarType.SEDAN, start, 2).isPresent());
        assertFalse(service.reserveCar("Frank", CarType.SUV, start, 2).isPresent());
        assertFalse(service.reserveCar("Grace", CarType.VAN, start, 2).isPresent());
    }

    @Test
    void testOverlappingReservationsRespectInventory() {
        // First reservation
        assertTrue(service.reserveCar("User1", CarType.SEDAN, start, 3).isPresent());

        // Overlapping reservation for same car type
        assertTrue(service.reserveCar("User2", CarType.SEDAN, start.plusDays(1), 3).isPresent());

        // Third overlapping reservation should fail (only 2 sedans)
        assertFalse(service.reserveCar("User3", CarType.SEDAN, start.plusDays(2), 2).isPresent());
    }
}

