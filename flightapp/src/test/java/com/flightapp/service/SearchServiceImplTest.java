package com.flightapp.service;

import com.flightapp.dto.*;
import com.flightapp.entity.*;
import com.flightapp.repository.InventoryRepository;
import com.flightapp.service.impl.SearchServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SearchServiceImplTest {

	@Mock
	private InventoryRepository inventoryRepository;

	private SearchServiceImpl searchService;

	@BeforeEach
	void setup() {
		searchService = new SearchServiceImpl(inventoryRepository);
	}

	@Test
	void testSearchReturnsAllClassesWhenNoClassTypeProvided() {
		Inventory inv = new Inventory();
		inv.setId(1L);
		Airline a = new Airline();
		a.setName("Air India");
		inv.setAirline(a);
		inv.setOrigin("BLR");
		inv.setDestination("DEL");
		inv.setDepartureDateTime(LocalDateTime.of(2025, 12, 20, 9, 30));
		inv.setArrivalDateTime(LocalDateTime.of(2025, 12, 20, 11, 45));
		SeatClass e = new SeatClass();
		e.setClassType("ECONOMY");
		e.setAvailableSeats(50);
		e.setPrice(4500.0);
		e.setInventory(inv);
		SeatClass b = new SeatClass();
		b.setClassType("BUSINESS");
		b.setAvailableSeats(10);
		b.setPrice(12000.0);
		b.setInventory(inv);
		inv.setSeatClasses(List.of(e, b));

		when(inventoryRepository.findByOriginAndDestinationAndDepartureDateTimeBetween(anyString(), anyString(), any(),
				any())).thenReturn(List.of(inv));

		SearchRequest req = new SearchRequest();
		req.setFrom("BLR");
		req.setTo("DEL");
		req.setDepartureDate(LocalDate.of(2025, 12, 20));

		var results = searchService.search(req);
		assertEquals(1, results.size());
		assertEquals(2, results.get(0).getClasses().size());
	}

	@Test
	void testSearchFiltersByClassType() {
		Inventory inv = new Inventory();
		inv.setId(1L);
		Airline a = new Airline();
		a.setName("Air India");
		inv.setAirline(a);
		inv.setOrigin("BLR");
		inv.setDestination("DEL");
		inv.setDepartureDateTime(LocalDateTime.of(2025, 12, 20, 9, 30));
		inv.setArrivalDateTime(LocalDateTime.of(2025, 12, 20, 11, 45));
		SeatClass e = new SeatClass();
		e.setClassType("ECONOMY");
		e.setAvailableSeats(50);
		e.setPrice(4500.0);
		e.setInventory(inv);
		SeatClass b = new SeatClass();
		b.setClassType("BUSINESS");
		b.setAvailableSeats(10);
		b.setPrice(12000.0);
		b.setInventory(inv);
		inv.setSeatClasses(List.of(e, b));

		when(inventoryRepository.findByOriginAndDestinationAndDepartureDateTimeBetween(anyString(), anyString(), any(),
				any())).thenReturn(List.of(inv));

		SearchRequest req = new SearchRequest();
		req.setFrom("BLR");
		req.setTo("DEL");
		req.setDepartureDate(LocalDate.of(2025, 12, 20));
		req.setClassType("BUSINESS");

		var results = searchService.search(req);
		assertEquals(1, results.size());
		assertEquals(1, results.get(0).getClasses().size());
		assertEquals("BUSINESS", results.get(0).getClasses().get(0).getClassType());
	}

	@Test
	void testSearchNoFlights() {
		when(inventoryRepository.findByOriginAndDestinationAndDepartureDateTimeBetween(anyString(), anyString(), any(),
				any())).thenReturn(List.of());
		SearchRequest req = new SearchRequest();
		req.setFrom("BLR");
		req.setTo("DEL");
		req.setDepartureDate(LocalDate.of(2025, 12, 20));
		var results = searchService.search(req);
		assertTrue(results.isEmpty());
	}
}
