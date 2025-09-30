package com.example.carrental.service;

import com.example.carrental.model.Car;
import com.example.carrental.model.CarType;
import com.example.carrental.model.Reservation;
import com.example.carrental.repository.CarRepository;
import com.example.carrental.repository.ReservationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CarRentalService {

    private final CarRepository carRepository;
    private final ReservationRepository reservationRepository;
    private static final Logger logger = LoggerFactory.getLogger(CarRentalService.class);

    public CarRentalService(CarRepository carRepository, ReservationRepository reservationRepository) {
        this.carRepository = carRepository;
        this.reservationRepository = reservationRepository;
    }

    @PostConstruct
    public void seedData() {
//        if (carRepository.count() == 0) {
//            carRepository.save(new Car(CarType.SEDAN));
//            carRepository.save(new Car(CarType.SUV));
//            carRepository.save(new Car(CarType.VAN));
//            carRepository.save(new Car(CarType.SEDAN));
//        }
//        System.out.println("Cars \n"+ carRepository.findByType(CarType.SEDAN));
//        System.out.println("Cars \n"+ carRepository.findByType(CarType.SUV));
//        System.out.println("Cars"+ carRepository.findByType(CarType.VAN));
//        return carRepository.count();
        logger.info("Adding initial cars");
        reservationRepository.deleteAll();
        carRepository.deleteAll();

        carRepository.save(new Car(CarType.SEDAN));
        carRepository.save(new Car(CarType.SEDAN));
        carRepository.save(new Car(CarType.SUV));
        carRepository.save(new Car(CarType.SUV));
        carRepository.save(new Car(CarType.SUV));
        carRepository.save(new Car(CarType.VAN));
        logger.info("Adding cars completed. Total cars: {}", carRepository.count());
    }

    @Transactional
    public Optional<Reservation> reserveCar(String userName, CarType type, LocalDateTime startDate, int days) {
        logger.info("Trying to reserve a {} for user {} starting at {} for {} days", type, userName, startDate, days);
        List<Car> availableCars = searchAvailableCars(type, startDate, days);
        if (availableCars.isEmpty()) {
            logger.warn("No available cars of type {}", type);
            return Optional.empty();
        }

        Car car = availableCars.get(0);
        Reservation reservation = new Reservation(car, userName, startDate, days);
        reservationRepository.save(reservation);
        logger.info("Reservation successful: {}", reservation);
        return Optional.of(reservation);
    }

    @Transactional(readOnly = true)
    public List<Car> searchAvailableCars(CarType type, LocalDateTime startDate, int days) {
        logger.debug("Searching available cars: type={}, start={}, days={}", type, startDate, days);
        LocalDateTime endDate = startDate.plusDays(days);
        return carRepository.findByType(type).stream()
                .filter(car -> {
                    List<Reservation> conflicts =
                            reservationRepository.findByCarAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                                    car, endDate, startDate);
                    return conflicts.isEmpty();
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Reservation> getAllReservations() {
        logger.debug("Attempting to fetch all the existing reservations");
        return reservationRepository.findAll();
    }

    @Transactional
    public boolean cancelReservation(Long reservationId) {
        logger.info("Attempting to cancel reservation id={}", reservationId);
        return reservationRepository.findById(reservationId)
                .map(reservation -> {
                    reservationRepository.delete(reservation);
                    logger.info("Cancelled reservation: {}", reservation);
                    return true;
                })
                .orElse(false);
    }
}
