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

	private LocalDate returnDate;
	private String tripType;
	private Integer passengers;
	private String classType;
	private Filter filters;
}

@Data
class Filter {
	private Double maxPrice;
	private List<String> airlines;
	private Boolean nonStopOnly;
}