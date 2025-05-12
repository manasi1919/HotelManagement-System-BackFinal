package com.hms.dto;

import java.util.Set;

import com.hms.model.Amenity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomResponseDTO {
	private Integer roomId;
	private Integer roomNumber;
	private Integer roomTypeId;
	private Boolean isAvailable;
	private Set<Amenity> amenities;
}
