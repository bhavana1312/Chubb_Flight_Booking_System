package com.flightapp.repository;

import com.flightapp.entity.Airline;
import com.flightapp.entity.Inventory;
import com.flightapp.entity.SeatClass;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class InventoryRepositoryTest {

	@Autowired
	private InventoryRepository inventoryRepository;

	@Autowired
	private AirlineRepository airlineRepository;

	@Test
	void testFindByOriginAndDestinationAndDepartureDateTimeBetween() {

		Airline airline = new Airline();
		airline.setCode("AI");
		airline.setName("Air India");
		airline = airlineRepository.save(airline);

		Inventory inv = new Inventory();
		inv.setAirline(airline);
		inv.setFlightNumber("AI202");
		inv.setOrigin("BLR");
		inv.setDestination("DEL");
		inv.setDepartureDateTime(LocalDateTime.of(2025, 12, 20, 10, 0));
		inv.setArrivalDateTime(LocalDateTime.of(2025, 12, 20, 12, 0));

		SeatClass sc = new SeatClass();
		sc.setClassType("ECONOMY");
		sc.setTotalSeats(100);
		sc.setAvailableSeats(100);
		sc.setPrice(4500.0);
		sc.setInventory(inv);

		inv.setSeatClasses(List.of(sc));

		inventoryRepository.save(inv);

		List<Inventory> results = inventoryRepository.findByOriginAndDestinationAndDepartureDateTimeBetween("BLR",
				"DEL", LocalDateTime.of(2025, 12, 20, 0, 0), LocalDateTime.of(2025, 12, 20, 23, 59));

		assertEquals(1, results.size());
		assertEquals("AI202", results.get(0).getFlightNumber());
		assertEquals("Air India", results.get(0).getAirline().getName());
	}
}
