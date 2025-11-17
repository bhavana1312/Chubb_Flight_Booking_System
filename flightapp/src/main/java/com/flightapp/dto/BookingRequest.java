package com.flightapp.dto;

import lombok.*;
import jakarta.validation.constraints.*;
import java.util.List;

@Data
public class BookingRequest {
	@Email
	@NotBlank
	private String email;
	@NotBlank
	private String contactName;
	@NotBlank
	private String classType;
	@NotNull
	private Integer seatsToBook;
	@NotNull
	private List<PassengerDTO> passengers;
	private PaymentDTO paymentInfo;
	private String specialRequests;
}
