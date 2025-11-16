package com.flightapp.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.flightapp.service.InventoryService;
import com.flightapp.dto.InventoryRequest;
import com.flightapp.entity.*;
import com.flightapp.repository.*;
import lombok.RequiredArgsConstructor;
import java.util.stream.Collectors;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {
	private final AirlineRepository airlineRepository;
	private final InventoryRepository inventoryRepository;
	private final SeatClassRepository seatClassRepository;

	@Transactional
	public Long addInventory(InventoryRequest req) {
		Airline airline = airlineRepository.findByCode(req.getAirlineCode());
		if (airline == null) {
			airline = new Airline();
			airline.setCode(req.getAirlineCode());
			airline.setName(req.getAirlineCode());
			airline = airlineRepository.save(airline);
		}
		Inventory inv = new Inventory();
		inv.setAirline(airline);
		inv.setFlightNumber(req.getFlightNumber());
		inv.setDepartureDateTime(req.getDepartureDateTime());
		inv.setArrivalDateTime(req.getArrivalDateTime());
		inv.setOrigin(req.getFrom());
		inv.setDestination(req.getTo());
		List<SeatClass> seatClasses = req.getSeatClasses().stream().map(sc -> {
			SeatClass s = new SeatClass();
			s.setClassType(sc.getClassType());
			s.setTotalSeats(sc.getTotalSeats());
			s.setAvailableSeats(sc.getTotalSeats());
			s.setPrice(sc.getPrice());
			s.setRefundable(Boolean.TRUE.equals(sc.getRefundable()));
			s.setBaggageLimitKg(sc.getBaggageLimitKg());
			s.setMealsIncluded(Boolean.TRUE.equals(sc.getMealsIncluded()));
			s.setInventory(inv);
			return s;
		}).collect(Collectors.toList());
		inv.setSeatClasses(seatClasses);
		Inventory saved = inventoryRepository.save(inv);
		return saved.getId();
	}
}
