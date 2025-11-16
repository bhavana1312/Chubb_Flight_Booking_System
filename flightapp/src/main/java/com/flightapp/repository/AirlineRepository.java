package com.flightapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flightapp.entity.Airline;

public interface AirlineRepository extends JpaRepository<Airline, Long> {
	Airline findByCode(String code);
}
