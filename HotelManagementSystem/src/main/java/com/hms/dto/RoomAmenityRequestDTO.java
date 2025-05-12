package com.hms.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomAmenityRequestDTO {
	private int roomId;
	private List<Integer> amenityId;
}
