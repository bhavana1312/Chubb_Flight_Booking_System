package com.flightapp.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "seat_class")
public class SeatClass {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	@JoinColumn(name = "inventory_id", nullable = false)
	private Inventory inventory;
	private String classType;
	private Integer totalSeats;
	private Integer availableSeats;
	private Double price;
	private Boolean refundable;
	private Integer baggageLimitKg;
	private Boolean mealsIncluded;
}
