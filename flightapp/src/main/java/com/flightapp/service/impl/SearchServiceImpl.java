package com.flightapp.service.impl;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.flightapp.service.SearchService;
import com.flightapp.dto.*;
import com.flightapp.repository.InventoryRepository;
import com.flightapp.entity.Inventory;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
	private final InventoryRepository inventoryRepository;

	@Override
	public List<SearchResultDTO> search(SearchRequest req) {

		LocalDate d = req.getDepartureDate();
		LocalDateTime start = d.atStartOfDay();
		LocalDateTime end = d.atTime(LocalTime.MAX);

		List<Inventory> inv = inventoryRepository.findByOriginAndDestinationAndDepartureDateTimeBetween(req.getFrom(),
				req.getTo(), start, end);

		return inv.stream().map(i -> {
			SearchResultDTO r = new SearchResultDTO();
			r.setFlightId(i.getId().toString());
			r.setAirlineName(i.getAirline().getName());
			r.setAirlineLogoUrl(i.getAirline().getLogoUrl());
			r.setDeparture(i.getDepartureDateTime());
			r.setArrival(i.getArrivalDateTime());
			r.setDuration(java.time.Duration.between(i.getDepartureDateTime(), i.getArrivalDateTime()).toMinutes());

			List<ClassOptionDTO> classes = i.getSeatClasses().stream().filter(sc -> {
				if (req.getClassType() == null || req.getClassType().isBlank()) {
					return true; // show all
				}
				return sc.getClassType().equalsIgnoreCase(req.getClassType());
			}).map(sc -> {
				ClassOptionDTO dto = new ClassOptionDTO();
				dto.setClassType(sc.getClassType());
				dto.setAvailableSeats(sc.getAvailableSeats());
				dto.setPrice(sc.getPrice());
				return dto;
			}).collect(Collectors.toList());

			r.setClasses(classes);
			return r;
		}).collect(Collectors.toList());
	}

}
