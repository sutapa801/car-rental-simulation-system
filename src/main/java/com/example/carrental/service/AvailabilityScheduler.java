package com.example.carrental.service;

import com.example.carrental.model.Car;
import com.example.carrental.model.CarType;
import com.example.carrental.model.Reservation;
import com.example.carrental.repository.CarRepository;
import com.example.carrental.repository.ReservationRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class AvailabilityScheduler {

    private final ReservationRepository reservationRepository;
    private final CarRepository carRepository;

    public AvailabilityScheduler(ReservationRepository reservationRepository, CarRepository carRepository) {
        this.reservationRepository = reservationRepository;
        this.carRepository = carRepository;
    }

    /**
     * Scheduled task to check for past reservations daily at midnight.
     * Optionally, it can archive or delete old reservations.
     */
    @Scheduled(cron = "0 0 0 * * ?") // every day at midnight
    @Transactional
    public void checkReservations() {
        LocalDate today = LocalDate.now();

        // Find all reservations that ended before today
        List<Reservation> pastReservations = reservationRepository.findByEndDateBefore(today);

        // Optional: remove or archive old reservations
        for (Reservation reservation : pastReservations) {
            // Example: delete past reservation
            // reservationRepository.delete(reservation);

            // Or just log it
            System.out.println("Past reservation: User=" + reservation.getUserName() +
                    ", CarID=" + reservation.getCar().getId() +
                    ", EndDate=" + reservation.getEndDate());
        }
    }
}
