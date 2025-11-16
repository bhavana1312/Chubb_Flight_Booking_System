package com.flightapp.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bookings")
public class Booking {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	@JoinColumn(name = "inventory_id", nullable = false)
	private Inventory inventory;
	private String pnr;
	private LocalDateTime bookingDate;
	private String contactEmail;
	private String contactName;
	private String classType;
	private Integer seatsBooked;
	private Double totalAmount;
	private String status;
	private String paymentTxn;
	private LocalDate travelDate;
	@OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	private List<Passenger> passengers;

	@PrePersist
	public void pre() {
		bookingDate = LocalDateTime.now();
	}
}
