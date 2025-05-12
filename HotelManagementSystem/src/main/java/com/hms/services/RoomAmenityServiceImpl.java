package com.hms.services;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hms.dto.RoomAmenityRequestDTO;
import com.hms.dto.RoomAmenityResponseDTO;
import com.hms.model.Amenity;
import com.hms.model.Room;
import com.hms.repository.AmenityRepository;
import com.hms.repository.RoomRepository;

import jakarta.transaction.Transactional;
@Service
public class RoomAmenityServiceImpl implements RoomAmenityServiceIntf {
	@Autowired
	private RoomRepository roomRepository;
	@Autowired
	private AmenityRepository amenityRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	@Transactional
	@Override
	public RoomAmenityResponseDTO associateAmenitiesToRoom(RoomAmenityRequestDTO roomAmenityRequestDTO) {
		Optional<Room> roomOpt = roomRepository.findById(roomAmenityRequestDTO.getRoomId());
		if(roomOpt.isPresent()) {
			Room room = roomOpt.get();
			List<Amenity> amenity = amenityRepository.findAllById(roomAmenityRequestDTO.getAmenityId());
			if (amenity.isEmpty()) {
                throw new RuntimeException("No amenities found for the provided IDs.");
            }
			//Convert the List to a Set (to avoid duplicates)
			Set<Amenity> amenitiesSet = new HashSet<>(amenity);
			room.setAmenities(amenitiesSet);
			roomRepository.save(room);
	        return modelMapper.map(amenitiesSet,RoomAmenityResponseDTO.class);

		}
		else {
			throw new RuntimeException("Room not found with ID"+ roomAmenityRequestDTO.getRoomId());
		}
		
	}

}
