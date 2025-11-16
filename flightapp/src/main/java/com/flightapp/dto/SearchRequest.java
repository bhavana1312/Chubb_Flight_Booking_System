package com.flightapp.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class SearchRequest {

	@NotBlank
	private String from;

	@NotBlank
	private String to;

	@NotNull
	private LocalDate departureDate;

	// Optional fields
	private LocalDate returnDate;
	private String tripType; // ONE_WAY or ROUND_TRIP
	private Integer passengers; // default = 1
	private String classType; // ECONOMY/BUSINESS/FIRST
	private Filter filters;
}

@Data
class Filter {
	private Double maxPrice;
	private List<String> airlines;
	private Boolean nonStopOnly;
}