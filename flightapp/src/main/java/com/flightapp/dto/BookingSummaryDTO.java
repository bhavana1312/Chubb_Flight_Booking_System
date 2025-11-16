package com.flightapp.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class BookingSummaryDTO {
    private String pnr;
    private LocalDate travelDate;
    private String from;
    private String to;
    private String status;
    private Double amount;
}
