package com.flightapp.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import jakarta.validation.constraints.*;

@Data
public class InventoryRequest {
	@NotBlank
	private String airlineCode;
	@NotBlank
	private String flightNumber;
	@NotBlank
	private String from;
	@NotBlank
	private String to;
	@NotNull
	private LocalDateTime departureDateTime;
	@NotNull
	private LocalDateTime arrivalDateTime;
	@NotNull
	private List<InventorySeatClassDTO> seatClasses;
	private List<String> mealOptions;
}

