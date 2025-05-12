package com.hms.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hms.dto.RoomRequestDTO;
import com.hms.dto.RoomResponseDTO;
import com.hms.execption.AmenityNotFoundException;
import com.hms.execption.ResourceNotFoundException;
import com.hms.execption.RoomException;
import com.hms.model.Room;
import com.hms.model.RoomType;
import com.hms.repository.AmenityRepository;
import com.hms.repository.RoomRepository;
import com.hms.repository.RoomTypeRepository;
@Service
public class RoomServicesImpl implements RoomServiceIntf{
	@Autowired
	RoomRepository roomRepository;
	
	 @Autowired
	 RoomTypeRepository roomTypeRepository;
	 
	 @Autowired
	 AmenityRepository amenityRepository;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Override
	public RoomResponseDTO newRoom(RoomRequestDTO roomRequestDto) {
		Room room=modelMapper.map(roomRequestDto, Room.class);
		room=roomRepository.save(room);
		
		return modelMapper.map(room, RoomResponseDTO.class);
	}

	@Override
	public List<RoomResponseDTO> findAllRooms() {
		
		return roomRepository.findAll()
				.stream()
				.map(room -> modelMapper.map(room, RoomResponseDTO.class))
				.collect(Collectors.toList());
	}

	@Override
	public RoomResponseDTO findRoomById(Integer roomId) {
		Optional<Room> room=roomRepository.findById(roomId);
		if(room.isPresent()) {
			return modelMapper.map(room, RoomResponseDTO.class);
		}
		else {
			throw new RoomException("Room not found with id: "+roomId);
		}
	}

	

	@Override
    public RoomResponseDTO updateById(Integer roomId, RoomRequestDTO roomRequestDto) {
        Optional<Room> roomOP = roomRepository.findById(roomId);
        if (roomOP.isPresent()) {
            Room room = roomOP.get();
            
            // Set the room type by fetching it from the RoomType repository
            RoomType roomType = roomTypeRepository.findById(roomRequestDto.getRoomTypeId())
                    .orElseThrow(() -> new ResourceNotFoundException("RoomType not found with id: " + roomRequestDto.getRoomTypeId()));
            
            // Set the room properties
            room.setRoomNumber(roomRequestDto.getRoomNumber());
            room.setIsAvailable(roomRequestDto.getIsAvailable());
            room.setRoomType(roomType);  // Set the RoomType object instead of just the ID
            
            roomRepository.save(room);
            
            return modelMapper.map(room, RoomResponseDTO.class);
        } else {
            throw new ResourceNotFoundException("Room not available with id: " + roomId);
        }
    }

	@Override
	public List<RoomResponseDTO> findByAvailableByRoomTypeId(Boolean isAvailable, Integer roomTypeId) {
		List<Room> rooms=roomRepository.findByIsAvailableAndRoomType_RoomTypeId(isAvailable,roomTypeId);
		if(!rooms.isEmpty()) {
			return rooms
					.stream()
					.map(room->modelMapper.map(room,RoomResponseDTO.class))
					.collect(Collectors.toList());
		}
		else {
			throw new RoomException("RoomType is Not Avaliable: "+roomTypeId);
		}
	}

	@Override
	public Boolean deleteById(Integer roomId) {
		Optional<Room> roomOP=roomRepository.findById(roomId);
		if(roomOP.isPresent()) {
			roomRepository.deleteById(roomId);
			return true;
		}
		else {
			throw new RoomException("RoomId is Not Avaliable with: "+roomId);
		}
	}

	//get room by Amenity
	@Override
	public List<RoomResponseDTO> findByAmenities_AmenityId(Integer amenityId ) {
		List<Room> roomOP=roomRepository.findByAmenities_AmenityId(amenityId);
		if(roomOP.size()>0) {
			return roomRepository.findByAmenities_AmenityId(amenityId)
					.stream()
					.map(amenity->modelMapper.map(amenity, RoomResponseDTO.class))
					.collect(Collectors.toList());
		}
		else {
			throw new RoomException("Room with amenityid is not avaliable "+amenityId);
		}
	}

	@Override
	public List<RoomResponseDTO> findByHotels_Location(String location) {
		List<Room> roomOp=roomRepository.findByAmenities_Hotels_Location(location);
		if(roomOp.size()>0) {
			return roomRepository.findByAmenities_Hotels_Location(location)
					.stream()
					.map(hotels->modelMapper.map(hotels, RoomResponseDTO.class))
					.collect(Collectors.toList());
		}
		else {
			throw new RoomException("Room in that location are not avaliable "+location);
		}
	}

	

	

}
