package com.flightapp.controller;

import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import com.flightapp.service.SearchService;
import com.flightapp.dto.SearchRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/v1.0/flight")
@RequiredArgsConstructor
public class SearchController {
	private final SearchService searchService;

	@PostMapping("/search")
	public ResponseEntity<?> search(@Valid @RequestBody SearchRequest req) {
		List<?> results = searchService.search(req);
		return ResponseEntity.ok(java.util.Map.of("results", results));
	}
}
