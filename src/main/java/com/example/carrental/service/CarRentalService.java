package com.example.carrental.service;

import com.example.carrental.model.Car;
import com.example.carrental.model.CarType;
import com.example.carrental.model.Reservation;
import com.example.carrental.repository.CarRepository;
import com.example.carrental.repository.ReservationRepository;
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

        reservationRepository.deleteAll();
        carRepository.deleteAll();

        carRepository.save(new Car(CarType.SEDAN));
        carRepository.save(new Car(CarType.SEDAN));
        carRepository.save(new Car(CarType.SUV));
        carRepository.save(new Car(CarType.VAN));
    }

    @Transactional
    public Optional<Reservation> reserveCar(String userName, CarType type, LocalDateTime startDate, int days) {
        List<Car> availableCars = searchAvailableCars(type, startDate, days);
        if (availableCars.isEmpty()) return Optional.empty();

        Car car = availableCars.get(0);
        Reservation reservation = new Reservation(car, userName, startDate, days);
        reservationRepository.save(reservation);
        return Optional.of(reservation);
    }

    @Transactional(readOnly = true)
    public List<Car> searchAvailableCars(CarType type, LocalDateTime startDate, int days) {
        LocalDateTime endDate = startDate.plusDays(days - 1);
        //Added to chcek what is returns during runtime
        /*List<Car> cars = carRepository.findByType(type);
        // Debug: check the actual runtime class of the list
        System.out.println("cars returned is of type: " + cars.getClass().getName());*/
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
        return reservationRepository.findAll();
    }

    @Transactional
    public boolean cancelReservation(Long reservationId) {
        return reservationRepository.findById(reservationId)
                .map(reservation -> {
                    reservationRepository.delete(reservation);
                    return true;
                })
                .orElse(false);
    }
}
