package com.flightapp.controller;

import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import com.flightapp.service.InventoryService;
import com.flightapp.dto.InventoryRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/v1.0/flight")
@RequiredArgsConstructor
public class InventoryController {
	private final InventoryService inventoryService;

	@PostMapping("/airline/inventory/add")
	public ResponseEntity<?> addInventory(@Valid @RequestBody InventoryRequest req) {
		Long id = inventoryService.addInventory(req);
		return ResponseEntity.status(201)
				.body(java.util.Map.of("status", "SUCCESS", "message", "Inventory added", "inventoryId", id));
	}
}
