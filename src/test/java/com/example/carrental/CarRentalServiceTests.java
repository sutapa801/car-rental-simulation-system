package com.example.carrental;

import com.example.carrental.model.Car;
import com.example.carrental.model.CarType;
import com.example.carrental.model.Reservation;
import com.example.carrental.service.CarRentalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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
        assertTrue(service.reserveCar("User1", CarType.SEDAN, start, 3).isPresent());
        assertTrue(service.reserveCar("User2", CarType.SEDAN, start, 3).isPresent());
        assertFalse(service.reserveCar("User3", CarType.SEDAN, start, 3).isPresent());
    }

    @Test
    void testSUVReservationsRespectInventory() {
        assertTrue(service.reserveCar("UserA", CarType.SUV, start, 2).isPresent());
        assertTrue(service.reserveCar("UserB", CarType.SUV, start, 2).isPresent());
        assertTrue(service.reserveCar("UserC", CarType.SUV, start, 2).isPresent());
        assertFalse(service.reserveCar("UserD", CarType.SUV, start, 2).isPresent());
    }

    @Test
    void testVanReservation() {
        assertTrue(service.reserveCar("UserX", CarType.VAN, start, 1).isPresent());
        assertFalse(service.reserveCar("UserY", CarType.VAN, start, 1).isPresent());
    }

    @Test
    void testMultipleUsersDifferentTypes() {
        assertTrue(service.reserveCar("Alice", CarType.SEDAN, start, 2).isPresent());
        assertTrue(service.reserveCar("Bob", CarType.SEDAN, start, 2).isPresent());
        assertTrue(service.reserveCar("Charlie", CarType.SUV, start, 2).isPresent());
        assertTrue(service.reserveCar("Diana", CarType.SUV, start, 2).isPresent());
        assertTrue(service.reserveCar("Eve", CarType.SUV, start, 2).isPresent());
        assertTrue(service.reserveCar("Frank", CarType.VAN, start, 2).isPresent());

        assertFalse(service.reserveCar("George", CarType.SEDAN, start, 2).isPresent());
        assertFalse(service.reserveCar("Hannah", CarType.SUV, start, 2).isPresent());
        assertFalse(service.reserveCar("Ivy", CarType.VAN, start, 2).isPresent());
    }

    @Test
    void testOverlappingReservationsRespectInventory() {
        assertTrue(service.reserveCar("User1", CarType.SEDAN, start, 3).isPresent());
        assertTrue(service.reserveCar("User2", CarType.SEDAN, start.plusDays(1), 3).isPresent());
        assertFalse(service.reserveCar("User3", CarType.SEDAN, start.plusDays(2), 2).isPresent());
    }

    @Test
    void testCancelReservation() {
        Reservation reservation = service.reserveCar("CancelUser", CarType.VAN, start, 2).orElseThrow();
        Long id = reservation.getId();

        assertTrue(service.cancelReservation(id), "Cancellation should succeed");
        assertFalse(service.cancelReservation(id), "Second cancel should fail (already removed)");
    }

    @Test
    void testSearchAvailableCars() {
        List<Car> initialSUVs = service.searchAvailableCars(CarType.SUV, start, 2);
        assertEquals(3, initialSUVs.size(), "Initially all 3 SUVs available");

        service.reserveCar("ResUser", CarType.SUV, start, 2);

        List<Car> afterReservation = service.searchAvailableCars(CarType.SUV, start, 2);
        assertEquals(2, afterReservation.size(), "One SUV should be unavailable now");
    }

    @Test
    void testGetAllReservations() {
        assertEquals(0, service.getAllReservations().size(), "Initially no reservations");

        Reservation res1 = service.reserveCar("User1", CarType.SEDAN, start, 2).orElseThrow();
        Reservation res2 = service.reserveCar("User2", CarType.SUV, start, 2).orElseThrow();

        List<Reservation> reservations = service.getAllReservations();
        assertEquals(2, reservations.size(), "Should return 2 reservations");
        assertTrue(reservations.stream().anyMatch(r -> r.getId().equals(res1.getId())));
        assertTrue(reservations.stream().anyMatch(r -> r.getId().equals(res2.getId())));
    }
}
