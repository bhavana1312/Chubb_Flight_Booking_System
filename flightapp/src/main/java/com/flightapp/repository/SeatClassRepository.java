package com.flightapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flightapp.entity.SeatClass;

import java.util.List;

public interface SeatClassRepository extends JpaRepository<SeatClass, Long> {
	List<SeatClass> findByInventoryId(Long inventoryId);

	SeatClass findByInventoryIdAndClassType(Long inventoryId, String classType);
}
