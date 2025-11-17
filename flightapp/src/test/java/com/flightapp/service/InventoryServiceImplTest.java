package com.flightapp.service;

import com.flightapp.dto.*;
import com.flightapp.entity.*;
import com.flightapp.repository.*;
import com.flightapp.service.impl.InventoryServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceImplTest {

    @Mock
    private AirlineRepository airlineRepository;

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private SeatClassRepository seatClassRepository;

    private InventoryServiceImpl inventoryService;

    @BeforeEach
    void setup() {
        inventoryService = new InventoryServiceImpl(airlineRepository, inventoryRepository, seatClassRepository);
    }

    private InventoryRequest sampleRequest() {
        InventoryRequest r = new InventoryRequest();
        r.setAirlineCode("AI");
        r.setFlightNumber("AI202");
        r.setFrom("BLR");
        r.setTo("DEL");
        r.setDepartureDateTime(LocalDateTime.of(2025,12,20,9,30));
        r.setArrivalDateTime(LocalDateTime.of(2025,12,20,11,45));
        InventorySeatClassDTO sc = new InventorySeatClassDTO();
        sc.setClassType("ECONOMY"); sc.setTotalSeats(150); sc.setPrice(4500.0);
        r.setSeatClasses(List.of(sc));
        return r;
    }

    @Test
    void testAddInventoryCreatesAirlineWhenMissing() {
        when(airlineRepository.findByCode("AI")).thenReturn(null);
        when(airlineRepository.save(any(Airline.class))).thenAnswer(a -> {
            Airline al = a.getArgument(0);
            al.setId(5L);
            return al;
        });
        when(inventoryRepository.save(any(Inventory.class))).thenAnswer(i -> {
            Inventory inv = i.getArgument(0);
            inv.setId(10L);
            return inv;
        });

        Long id = inventoryService.addInventory(sampleRequest());
        assertEquals(10L, id);
        verify(inventoryRepository, times(1)).save(any(Inventory.class));
    }

    @Test
    void testAddInventoryUsesExistingAirline() {
        Airline existing = new Airline();
        existing.setId(3L);
        when(airlineRepository.findByCode("AI")).thenReturn(existing);
        when(inventoryRepository.save(any(Inventory.class))).thenAnswer(i -> {
            Inventory inv = i.getArgument(0);
            inv.setId(11L);
            return inv;
        });

        Long id = inventoryService.addInventory(sampleRequest());
        assertEquals(11L, id);
        verify(airlineRepository, never()).save(any(Airline.class));
    }
}
