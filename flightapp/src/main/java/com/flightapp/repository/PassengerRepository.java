package com.flightapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flightapp.entity.Passenger;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {
}
