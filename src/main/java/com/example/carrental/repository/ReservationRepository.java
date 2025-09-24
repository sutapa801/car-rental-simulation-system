package com.example.carrental.repository;

import com.example.carrental.model.Car;
import com.example.carrental.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByCarAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Car car, LocalDate requestedEnd, LocalDate requestedStart);

    List<Reservation> findByEndDateBefore(LocalDate date);
}
