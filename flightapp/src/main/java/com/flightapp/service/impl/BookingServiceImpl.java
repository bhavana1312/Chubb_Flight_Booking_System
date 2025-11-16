package com.flightapp.service.impl;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import com.flightapp.service.BookingService;
import com.flightapp.dto.*;
import com.flightapp.repository.*;
import com.flightapp.entity.*;
import com.flightapp.util.PnrUtil;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
	private final InventoryRepository inventoryRepository;
	private final SeatClassRepository seatClassRepository;
	private final BookingRepository bookingRepository;

	@Transactional
	public BookingResponse book(Long flightId, BookingRequest req) {
		Inventory inv = inventoryRepository.findById(flightId)
				.orElseThrow(() -> new RuntimeException("Flight not found"));
		SeatClass sc = seatClassRepository.findByInventoryIdAndClassType(inv.getId(), req.getClassType());
		if (sc == null)
			throw new RuntimeException("Class not available");
		if (sc.getAvailableSeats() < req.getSeatsToBook())
			throw new RuntimeException("SeatsUnavailable");
		sc.setAvailableSeats(sc.getAvailableSeats() - req.getSeatsToBook());
		seatClassRepository.save(sc);
		Booking booking = new Booking();
		booking.setInventory(inv);
		String pnr = PnrUtil.generate();
		booking.setPnr(pnr);
		booking.setContactEmail(req.getEmail());
		booking.setContactName(req.getContactName());
		booking.setClassType(req.getClassType());
		booking.setSeatsBooked(req.getSeatsToBook());
		double total = req.getPassengers().stream().mapToDouble(p -> sc.getPrice()).sum();
		booking.setTotalAmount(total);
		booking.setStatus("BOOKED");
		booking.setTravelDate(inv.getDepartureDateTime().toLocalDate());
		List<Passenger> passengers = req.getPassengers().stream().map(pdto -> {
			Passenger p = new Passenger();
			p.setBooking(booking);
			p.setName(pdto.getName());
			p.setAge(pdto.getAge());
			p.setGender(pdto.getGender());
			p.setSeatNumber(pdto.getSeatNumber());
			p.setMeal(pdto.getMeal());
			return p;
		}).collect(Collectors.toList());
		booking.setPassengers(passengers);
		Booking saved = bookingRepository.save(booking);
		BookingResponse resp = new BookingResponse();
		resp.setStatus(saved.getStatus());
		resp.setPnr(saved.getPnr());
		resp.setBookingId("BK-" + saved.getId());
		resp.setTotalAmount(saved.getTotalAmount());
		List<TicketInfo> tickets = saved.getPassengers().stream().map(p -> {
			TicketInfo t = new TicketInfo();
			t.setPassenger(p.getName());
			t.setSeat(p.getSeatNumber());
			t.setTicketId("TCK-" + p.getId());
			return t;
		}).collect(Collectors.toList());
		resp.setTickets(tickets);
		resp.setDownloadUrl("/api/v1.0/flight/ticket/download/" + saved.getPnr());
		return resp;
	}

	public BookingResponse getByPnr(String pnr) {
		Booking b = bookingRepository.findByPnr(pnr);
		if (b == null)
			throw new RuntimeException("Not found");
		BookingResponse resp = new BookingResponse();
		resp.setStatus(b.getStatus());
		resp.setPnr(b.getPnr());
		resp.setBookingId("BK-" + b.getId());
		resp.setTotalAmount(b.getTotalAmount());
		return resp;
	}

	public java.util.List<BookingSummaryDTO> history(String email) {
		return bookingRepository.findByContactEmailOrderByBookingDateDesc(email).stream().map(b -> {
			BookingSummaryDTO s = new BookingSummaryDTO();
			s.setPnr(b.getPnr());
			s.setTravelDate(b.getTravelDate());
			s.setFrom(b.getInventory().getOrigin());
			s.setTo(b.getInventory().getDestination());
			s.setStatus(b.getStatus());
			s.setAmount(b.getTotalAmount());
			return s;
		}).collect(Collectors.toList());
	}

	@Transactional
	public BookingResponse cancel(String pnr) {

		Booking b = bookingRepository.findByPnr(pnr);
		if (b == null)
			throw new RuntimeException("Booking not found");
		LocalDate travel = b.getTravelDate();
		if (LocalDate.now().isAfter(travel.minusDays(1))) {
			throw new RuntimeException("Cannot cancel within 24 hours of travel");
		}
		SeatClass sc = seatClassRepository.findByInventoryIdAndClassType(b.getInventory().getId(), b.getClassType());

		if (sc != null) {
			sc.setAvailableSeats(sc.getAvailableSeats() + b.getSeatsBooked());
			seatClassRepository.save(sc);
		}
		b.setStatus("CANCELLED");
		bookingRepository.save(b);
		BookingResponse resp = new BookingResponse();
		resp.setStatus("CANCELLED");
		resp.setPnr(b.getPnr());
		resp.setBookingId("BK-" + b.getId());
		resp.setTotalAmount(b.getTotalAmount() * 0.95); // refund 95%

		return resp;
	}
}
