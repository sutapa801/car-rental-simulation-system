package com.example.carrental.controller;

import com.example.carrental.model.Car;
import com.example.carrental.model.CarType;
import com.example.carrental.model.Reservation;
import com.example.carrental.service.CarRentalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
public class CarRentalController {
    private static final Logger logger = LoggerFactory.getLogger(CarRentalController.class);

    private final CarRentalService service;

    public CarRentalController(CarRentalService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("types", CarType.values());
        return "index";
    }

    @PostMapping("/search")
    public String searchCars(@RequestParam CarType type,
                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
                             @RequestParam int days,
                             Model model) {
        logger.info("Received search request: type={}, start={}, days={}", type, start, days);
        List<Car> availableCars = service.searchAvailableCars(type, start, days);
        model.addAttribute("availableCars", availableCars);
        model.addAttribute("type", type);
        model.addAttribute("start", start);
        model.addAttribute("days", days);
        return "searchResults";
    }

    @PostMapping("/reserve")
    public String reserve(@RequestParam String userName,
                          @RequestParam CarType type,
                          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime date,
                          @RequestParam int days,
                          Model model) {
        logger.info("Received reservation request from user {}", userName);
        Optional<Reservation> reservation = service.reserveCar(userName, type, date, days);
        model.addAttribute("success", reservation.isPresent());
        model.addAttribute("types", CarType.values());
        return "index";
    }

    @GetMapping("/reservations")
    public String reservations(Model model) {
        model.addAttribute("reservations", service.getAllReservations());
        return "reservations";
    }

    @PostMapping("/cancel")
    public String cancelReservation(@RequestParam Long reservationId, Model model) {
        logger.info("Received cancel request");
        boolean success = service.cancelReservation(reservationId);
        model.addAttribute("successCancel", success);
        model.addAttribute("reservations", service.getAllReservations());
        return "reservations";
    }
}
