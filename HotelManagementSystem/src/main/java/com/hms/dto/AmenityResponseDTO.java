package com.hms.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

public class AmenityResponseDTO {
	private Integer amenityId;
	 private String name;
	 private String description;
	public AmenityResponseDTO(Integer amenityId, String name, String description, List<RoomResponseDTO> rooms) {
		super();
		this.amenityId = amenityId;
		this.name = name;
		this.description = description;
		this.rooms = rooms;
	}
	
	public AmenityResponseDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	private List<RoomResponseDTO> rooms;

}
