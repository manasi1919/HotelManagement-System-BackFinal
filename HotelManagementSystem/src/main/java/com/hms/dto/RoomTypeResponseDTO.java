package com.hms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomTypeResponseDTO {
	 private int roomTypeId;
	    private String typeName;
	    private String description;
	    private Double pricePerNight;
	    private Integer maxOccupancy;}

