package com.flightapp.dto;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class InventorySeatClassDTO {
	@NotBlank
	private String classType;

	@NotNull
	private Integer totalSeats;

	@NotNull
	private Double price;

	private Boolean refundable;
	private Integer baggageLimitKg;
	private Boolean mealsIncluded;
}
