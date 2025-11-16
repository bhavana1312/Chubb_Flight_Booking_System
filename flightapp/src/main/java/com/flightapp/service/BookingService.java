package com.flightapp.service;

import com.flightapp.dto.BookingRequest;
import com.flightapp.dto.BookingResponse;

public interface BookingService {
	BookingResponse book(Long flightId, BookingRequest req);

	BookingResponse getByPnr(String pnr);

	java.util.List<com.flightapp.dto.BookingSummaryDTO> history(String email);

	BookingResponse cancel(String pnr);
}
