package com.flightapp.dto;

import lombok.Data;
import java.util.List;

@Data
public class BookingResponse {
	private String status;
	private String pnr;
	private String bookingId;
	private Double totalAmount;
	private List<TicketInfo> tickets;
	private String downloadUrl;
}
