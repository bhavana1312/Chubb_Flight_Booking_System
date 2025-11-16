package com.flightapp.controller;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import com.flightapp.service.BookingService;
import com.flightapp.dto.BookingRequest;
import com.flightapp.dto.BookingResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/v1.0/flight")
@RequiredArgsConstructor
public class BookingController{
  private final BookingService bookingService;

  @PostMapping("/booking/{flightId}")
  public ResponseEntity<?> book(@PathVariable Long flightId,@Valid @RequestBody BookingRequest req){
    BookingResponse resp=bookingService.book(flightId,req);
    return ResponseEntity.status(201).body(resp);
  }

  @GetMapping("/ticket/{pnr}")
  public ResponseEntity<?> getTicket(@PathVariable String pnr){
    BookingResponse resp=bookingService.getByPnr(pnr);
    return ResponseEntity.ok(resp);
  }

  @GetMapping("/booking/history/{emailId}")
  public ResponseEntity<?> history(@PathVariable String emailId){
    return ResponseEntity.ok(bookingService.history(emailId));
  }

  @DeleteMapping("/booking/cancel/{pnr}")
  public ResponseEntity<?> cancel(@PathVariable String pnr){
    return ResponseEntity.ok(bookingService.cancel(pnr));
  }
}
