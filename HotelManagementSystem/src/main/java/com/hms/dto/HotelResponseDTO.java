package com.hms.dto;

import java.util.List;

import com.hms.model.Amenity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelResponseDTO {
	private int hotelId;
    private String name;
    private String location;
    private String description;
    private List<Amenity> amenities;
    
}
