package com.flightapp.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class SearchResultDTO {
    private String flightId;
    private String airlineName;
    private String airlineLogoUrl;
    private LocalDateTime departure;
    private LocalDateTime arrival;
    private long duration;
    private List<ClassOptionDTO> classes;
}
