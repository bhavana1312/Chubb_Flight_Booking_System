package com.flightapp.service;

import com.flightapp.dto.SearchRequest;
import java.util.List;
import com.flightapp.dto.SearchResultDTO;

public interface SearchService {
	List<SearchResultDTO> search(SearchRequest req);
}
