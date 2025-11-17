package com.flightapp.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class PaymentDTO {
	@NotBlank
	private String method;

	private String transactionId;
	private String cardLast4;
}
