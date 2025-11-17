package com.flightapp.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flightapp.dto.BookingRequest;
import com.flightapp.dto.BookingResponse;
import com.flightapp.dto.PassengerDTO;
import com.flightapp.service.BookingService;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void testBookEndpoint() throws Exception {
        BookingRequest req = new BookingRequest();
        req.setEmail("a@b");
        req.setContactName("x");
        req.setClassType("ECONOMY");
        req.setSeatsToBook(1);
        req.setPassengers(List.of(new PassengerDTO(){{
            setName("p"); setAge(20); setGender("M");
        }}));

        BookingResponse resp = new BookingResponse();
        resp.setStatus("BOOKED");
        resp.setPnr("PNR1");
        when(bookingService.book(eq(1L), any(BookingRequest.class))).thenReturn(resp);

        mvc.perform(post("/api/v1.0/flight/booking/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andExpect(status().isCreated());
    }

    @Test
    void testCancelEndpoint() throws Exception {
        BookingResponse resp = new BookingResponse();
        resp.setStatus("CANCELLED");
        when(bookingService.cancel("PNR1")).thenReturn(resp);

        mvc.perform(delete("/api/v1.0/flight/booking/cancel/PNR1"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetTicket() throws Exception {
        BookingResponse resp = new BookingResponse();
        resp.setPnr("PNR1");
        when(bookingService.getByPnr("PNR1")).thenReturn(resp);

        mvc.perform(get("/api/v1.0/flight/ticket/PNR1"))
                .andExpect(status().isOk());
    }
}
