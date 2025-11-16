package com.flightapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flightapp.entity.Booking;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
	Booking findByPnr(String pnr);

	List<Booking> findByContactEmailOrderByBookingDateDesc(String email);
}
