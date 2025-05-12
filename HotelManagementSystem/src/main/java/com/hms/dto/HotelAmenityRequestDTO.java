package com.hms.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelAmenityRequestDTO {
    private Integer hotelId;
    private List<Integer> amenityId;
    
}
