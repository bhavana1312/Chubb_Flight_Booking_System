package com.flightapp.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "passengers")
public class Passenger {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	@JoinColumn(name = "booking_id", nullable = false)
	private Booking booking;
	private String name;
	private String gender;
	private Integer age;
	private String seatNumber;
	private String meal;
}
