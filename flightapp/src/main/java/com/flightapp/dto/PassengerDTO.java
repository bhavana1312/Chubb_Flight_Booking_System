package com.flightapp.dto;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class PassengerDTO {
	@NotBlank
	private String name;

	@NotBlank
	private String gender;

	@NotNull
	private Integer age;

	private String seatNumber;
	private String meal;
}
