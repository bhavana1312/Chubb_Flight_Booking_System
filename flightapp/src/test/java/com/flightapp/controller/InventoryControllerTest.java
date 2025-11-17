package com.flightapp.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flightapp.service.InventoryService;

@WebMvcTest(controllers = InventoryController.class)
@AutoConfigureMockMvc(addFilters = false)
class InventoryControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private InventoryService inventoryService;
    @Autowired
    private ObjectMapper mapper;

    @Test
    void testAddInventoryReturnsCreated() throws Exception {
    	String body = """
                {
                  "airlineCode": "AI",
                  "flightNumber": "AI202",
                  "from": "BLR",
                  "to": "DEL",
                  "departureDateTime": "2025-12-20T09:30:00",
                  "arrivalDateTime": "2025-12-20T11:45:00",
                  "seatClasses": [
                    {
                      "classType": "ECONOMY",
                      "totalSeats": 150,
                      "price": 4500,
                      "refundable": false,
                      "baggageLimitKg": 15,
                      "mealsIncluded": false
                    }
                  ]
                }
                """;
    	when(inventoryService.addInventory(any())).thenReturn(1L);

        mvc.perform(
                post("/api/v1.0/flight/airline/inventory/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());
    }
}
