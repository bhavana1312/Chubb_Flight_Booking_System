package com.flightapp.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "inventory")
public class Inventory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	@JoinColumn(name = "airline_id", nullable = false)
	private Airline airline;
	private String flightNumber;
	private LocalDateTime departureDateTime;
	private LocalDateTime arrivalDateTime;
	private String origin;
	private String destination;
	private LocalDateTime createdAt;
	@OneToMany(mappedBy = "inventory", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	private List<SeatClass> seatClasses;

	@PrePersist
	public void pre() {
		createdAt = LocalDateTime.now();
	}
}
