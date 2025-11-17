package com.flightapp.controller;

import com.flightapp.service.SearchService;
import com.flightapp.dto.SearchRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SearchController.class)
class SearchControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private SearchService searchService;
    @Autowired
    private ObjectMapper mapper;

    @Test
    void testSearchReturns200() throws Exception {
        SearchRequest req = new SearchRequest();
        req.setFrom("BLR"); req.setTo("DEL"); req.setDepartureDate(java.time.LocalDate.of(2025,12,20));
        when(searchService.search(any())).thenReturn(java.util.List.of());

        mvc.perform(post("/api/v1.0/flight/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }
}
