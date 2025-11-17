package com.flightapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.flightapp.dto.BookingRequest;
import com.flightapp.dto.BookingResponse;
import com.flightapp.dto.PassengerDTO;
import com.flightapp.dto.PaymentDTO;
import com.flightapp.entity.Airline;
import com.flightapp.entity.Booking;
import com.flightapp.entity.Inventory;
import com.flightapp.entity.Passenger;
import com.flightapp.entity.SeatClass;
import com.flightapp.repository.BookingRepository;
import com.flightapp.repository.InventoryRepository;
import com.flightapp.repository.SeatClassRepository;
import com.flightapp.service.impl.BookingServiceImpl;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

	@Mock
	private InventoryRepository inventoryRepository;

	@Mock
	private SeatClassRepository seatClassRepository;

	@Mock
	private BookingRepository bookingRepository;

	// IMPORTANT: BookingServiceImpl constructor order is (InventoryRepository,
	// SeatClassRepository, BookingRepository)
	private BookingServiceImpl bookingService;

	@BeforeEach
	void setup() {
		bookingService = new BookingServiceImpl(inventoryRepository, seatClassRepository, bookingRepository);
	}

	private Inventory sampleInventory() {
		Inventory inv = new Inventory();
		inv.setId(1L);
		inv.setDepartureDateTime(LocalDateTime.of(2025, 12, 20, 9, 30));
		inv.setArrivalDateTime(LocalDateTime.of(2025, 12, 20, 11, 45));
		inv.setOrigin("BLR");
		inv.setDestination("DEL");
		Airline a = new Airline();
		a.setName("Air India");
		inv.setAirline(a);
		return inv;
	}

	private SeatClass sampleSeatClass() {
		SeatClass sc = new SeatClass();
		sc.setClassType("ECONOMY");
		sc.setAvailableSeats(10);
		sc.setPrice(4500.0);
		return sc;
	}

	private BookingRequest sampleBookingRequest() {
		BookingRequest req = new BookingRequest();
		req.setEmail("bhavana@gmail.com");
		req.setContactName("Bhavana");
		req.setClassType("ECONOMY");
		req.setSeatsToBook(2);
		PassengerDTO p1 = new PassengerDTO();
		p1.setName("Asha");
		p1.setAge(28);
		p1.setGender("F");
		p1.setSeatNumber("12A");
		p1.setMeal("VEG");
		PassengerDTO p2 = new PassengerDTO();
		p2.setName("Ravi");
		p2.setAge(30);
		p2.setGender("M");
		p2.setSeatNumber("12B");
		p2.setMeal("NON_VEG");
		req.setPassengers(List.of(p1, p2));
		PaymentDTO pay = new PaymentDTO();
		pay.setMethod("CARD");
		pay.setCardLast4("4242");
		req.setPaymentInfo(pay);
		return req;
	}

	@Test
	void testBookSuccess() {
		Inventory inv = sampleInventory();
		when(inventoryRepository.findById(1L)).thenReturn(Optional.of(inv));
		SeatClass sc = sampleSeatClass();
		sc.setInventory(inv);
		when(seatClassRepository.findByInventoryIdAndClassType(inv.getId(), "ECONOMY")).thenReturn(sc);

		// ensure bookingRepository.save returns booking populated with an id and
		// passenger ids
		when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> {
			Booking b = invocation.getArgument(0);
			b.setId(100L);
			List<Passenger> ps = b.getPassengers();
			long id = 1L;
			for (Passenger p : ps) {
				p.setId(id++);
			}
			return b;
		});

		BookingResponse resp = bookingService.book(1L, sampleBookingRequest());

		assertNotNull(resp);
		assertEquals("BOOKED", resp.getStatus());
		assertNotNull(resp.getPnr());
		assertEquals(2, resp.getTickets().size());
		verify(seatClassRepository, times(1)).save(sc);
		assertEquals(8, sc.getAvailableSeats());
	}

	@Test
	void testBookFlightNotFound() {
		when(inventoryRepository.findById(99L)).thenReturn(Optional.empty());
		BookingRequest req = sampleBookingRequest();
		RuntimeException ex = assertThrows(RuntimeException.class, () -> bookingService.book(99L, req));
		assertTrue(ex.getMessage().contains("Flight not found"));
	}

	@Test
	void testBookClassNotAvailable() {
		Inventory inv = sampleInventory();
		when(inventoryRepository.findById(1L)).thenReturn(Optional.of(inv));
		when(seatClassRepository.findByInventoryIdAndClassType(inv.getId(), "ECONOMY")).thenReturn(null);
		BookingRequest req = sampleBookingRequest();
		RuntimeException ex = assertThrows(RuntimeException.class, () -> bookingService.book(1L, req));
		assertTrue(ex.getMessage().contains("Class not available"));
	}

	@Test
	void testBookSeatsUnavailable() {
		Inventory inv = sampleInventory();
		when(inventoryRepository.findById(1L)).thenReturn(Optional.of(inv));
		SeatClass sc = sampleSeatClass();
		sc.setAvailableSeats(1); // less than requested
		when(seatClassRepository.findByInventoryIdAndClassType(inv.getId(), "ECONOMY")).thenReturn(sc);
		BookingRequest req = sampleBookingRequest();
		RuntimeException ex = assertThrows(RuntimeException.class, () -> bookingService.book(1L, req));
		assertTrue(ex.getMessage().contains("Seats unavailable"));
	}

	@Test
	void testGetByPnrNotFound() {
		when(bookingRepository.findByPnr("X")).thenReturn(null);
		RuntimeException ex = assertThrows(RuntimeException.class, () -> bookingService.getByPnr("X"));
		assertTrue(ex.getMessage().contains("Not found"));
	}

	@Test
	void testHistoryMapping() {
		Booking b = new Booking();
		b.setPnr("PNR1");
		b.setTravelDate(LocalDate.of(2025, 12, 20));
		Inventory inv = sampleInventory();
		b.setInventory(inv);
		b.setStatus("BOOKED");
		b.setTotalAmount(4500.0);
		when(bookingRepository.findByContactEmailOrderByBookingDateDesc("a@b")).thenReturn(List.of(b));
		var list = bookingService.history("a@b");
		assertEquals(1, list.size());
		assertEquals("PNR1", list.get(0).getPnr());
	}

	@Test
	void testCancelSuccessRestoresSeats() {
		Booking b = new Booking();
		b.setPnr("PNR123");
		b.setSeatsBooked(2);
		b.setClassType("ECONOMY");
		b.setTravelDate(LocalDate.now().plusDays(5));
		b.setTotalAmount(10000.0);
		Inventory inv = sampleInventory();
		inv.setId(1L);
		b.setInventory(inv);

		when(bookingRepository.findByPnr("PNR123")).thenReturn(b);

		SeatClass sc = sampleSeatClass();
		sc.setAvailableSeats(5);
		when(seatClassRepository.findByInventoryIdAndClassType(1L, "ECONOMY")).thenReturn(sc);

		BookingResponse resp = bookingService.cancel("PNR123");

		assertEquals("CANCELLED", resp.getStatus());
		assertEquals(7, sc.getAvailableSeats());
		verify(seatClassRepository, times(1)).save(sc);
		verify(bookingRepository, times(1)).save(b);
	}

	@Test
	void testCancelTooLate() {
		Booking b = new Booking();
		b.setPnr("PNR2");
		b.setTravelDate(LocalDate.now().plusDays(0)); // today
		when(bookingRepository.findByPnr("PNR2")).thenReturn(b);
		RuntimeException ex = assertThrows(RuntimeException.class, () -> bookingService.cancel("PNR2"));
		assertTrue(ex.getMessage().contains("Cannot cancel within 24 hours"));
	}
}
