package com.hms.services;

import java.util.List;

import com.hms.dto.RoomRequestDTO;
import com.hms.dto.RoomResponseDTO;
import com.hms.model.Room;

public interface RoomServiceIntf {
	  RoomResponseDTO newRoom(RoomRequestDTO roomRequestDto);
	    List<RoomResponseDTO> findAllRooms();
	    RoomResponseDTO findRoomById(Integer roomId);
	    RoomResponseDTO updateById(Integer roomId, RoomRequestDTO roomRequestDto);
	    List<RoomResponseDTO> findByAvailableByRoomTypeId(Boolean isAvailable, Integer roomTypeId);
	    Boolean deleteById(Integer roomId);
	    List<RoomResponseDTO> findByAmenities_AmenityId(Integer amenityId);
	    List<RoomResponseDTO> findByHotels_Location(String location);
	    //List<Room> findByAmenityId(Integer AmenityId);
	    
}
