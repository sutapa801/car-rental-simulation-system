package com.example.carrental.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;
    private LocalDate startDate;
    private LocalDate endDate;
    private int days;

    @ManyToOne
    private Car car;

    public Reservation() {}

    public Reservation(Car car, String userName, LocalDate startDate, int days) {
        this.car = car;
        this.userName = userName;
        this.startDate = startDate;
        this.days = days;
        this.endDate = startDate.plusDays(days - 1);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public int getDays() { return days; }
    public void setDays(int days) { this.days = days; }

    public Car getCar() { return car; }
    public void setCar(Car car) { this.car = car; }
}
