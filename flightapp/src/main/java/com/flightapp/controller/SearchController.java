package com.flightapp.controller;

import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import com.flightapp.service.SearchService;
import com.flightapp.dto.SearchRequest;
import com.flightapp.dto.SearchResultDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1.0/flight")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @PostMapping("/search")
    public ResponseEntity<Map<String, List<SearchResultDTO>>> search(
            @Valid @RequestBody SearchRequest req) {

        List<SearchResultDTO> results = searchService.search(req);
        return ResponseEntity.ok(Map.of("results", results));
    }
}
