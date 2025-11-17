package com.flightapp.controller;

import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import com.flightapp.service.BookingService;
import com.flightapp.dto.BookingRequest;
import com.flightapp.dto.BookingResponse;
import com.flightapp.dto.BookingSummaryDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/v1.0/flight")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping("/booking/{flightId}")
    public ResponseEntity<BookingResponse> book(
            @PathVariable Long flightId,
            @Valid @RequestBody BookingRequest req) {

        BookingResponse resp = bookingService.book(flightId, req);
        return ResponseEntity.status(201).body(resp);
    }

    @GetMapping("/ticket/{pnr}")
    public ResponseEntity<BookingResponse> getTicket(@PathVariable String pnr) {
		return ResponseEntity.ok(bookingService.getByPnr(pnr));
    }

    @GetMapping("/booking/history/{emailId}")
    public ResponseEntity<List<BookingSummaryDTO>> history(@PathVariable String emailId) {
        return ResponseEntity.ok(bookingService.history(emailId));
    }

    @DeleteMapping("/booking/cancel/{pnr}")
    public ResponseEntity<BookingResponse> cancel(@PathVariable String pnr) {
        return ResponseEntity.ok(bookingService.cancel(pnr));
    }
}
