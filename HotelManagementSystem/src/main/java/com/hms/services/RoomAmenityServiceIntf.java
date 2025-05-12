
package com.hms.services;

import com.hms.dto.RoomAmenityRequestDTO;
import com.hms.dto.RoomAmenityResponseDTO;

public interface RoomAmenityServiceIntf {
	RoomAmenityResponseDTO associateAmenitiesToRoom(RoomAmenityRequestDTO roomRequestDTO);

}
