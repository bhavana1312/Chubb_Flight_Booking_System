package com.flightapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flightapp.entity.Inventory;

import java.time.LocalDateTime;
import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
	List<Inventory> findByOriginAndDestinationAndDepartureDateTimeBetween(String origin, String destination,
			LocalDateTime start, LocalDateTime end);
}
