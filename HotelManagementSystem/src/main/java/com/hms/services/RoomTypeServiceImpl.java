package com.hms.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hms.dto.RoomTypeRequestDTO;
import com.hms.dto.RoomTypeResponseDTO;
import com.hms.execption.RoomTypeException;
import com.hms.model.RoomType;
import com.hms.repository.RoomTypeRepository;
@Service
public class RoomTypeServiceImpl implements RoomTypeServicIntf{
	@Autowired
	RoomTypeRepository roomTypeRepository;
	
	@Autowired
	ModelMapper modelMapper;

	@Override
	public List<RoomTypeResponseDTO> findAllRoomType() {
		
		return roomTypeRepository.findAll()
				.stream()
				.map(roomType->modelMapper.map(roomType,RoomTypeResponseDTO.class))
				.collect(Collectors.toList());
	}

	@Override
	public RoomTypeResponseDTO newRoomType(RoomTypeRequestDTO roomTypeRequestDto) {
		RoomType roomType=roomTypeRepository.save(modelMapper.map(roomTypeRequestDto, RoomType.class));
		return modelMapper.map(roomType, RoomTypeResponseDTO.class);
	}

	@Override
	public RoomTypeResponseDTO updateByRoomTypeId(Integer roomTypeId, RoomTypeRequestDTO roomTypeRequestDto) {
		Optional<RoomType>  roomTypeOp=roomTypeRepository.findById(roomTypeId);
		if(roomTypeOp!=null) {
			RoomType roomType=roomTypeOp.get();
			roomType.setTypeName(roomTypeRequestDto.getTypeName());
			roomType.setDescription(roomTypeRequestDto.getDescription());
			roomType.setMaxOccupancy(roomTypeRequestDto.getMaxOccupancy());
			roomType.setPricePerNight(roomTypeRequestDto.getPricePerNight());
			roomTypeRepository.save(roomType);
			return modelMapper.map(roomType, RoomTypeResponseDTO.class);
		}
		else {
			throw new RoomTypeException("Room type with id"+roomTypeId+"not found");
		}
	}

	@Override
	public RoomTypeResponseDTO findByRoomTypeId(Integer roomTypeId) {
		Optional<RoomType>  roomTypeOp=roomTypeRepository.findById(roomTypeId);
		if(roomTypeOp!=null) {
			return modelMapper.map(roomTypeOp, RoomTypeResponseDTO.class);
		}
		else {
			throw new RoomTypeException("Room type with id"+roomTypeId+"not found");
		}
	}

	@Override
	public Boolean deleteByRoomTypeId(Integer roomTypeId) {
		Optional<RoomType>  roomTypeOp=roomTypeRepository.findById(roomTypeId);
		if(roomTypeOp!=null) {
			roomTypeRepository.deleteById(roomTypeId);
			return true;
		}
		else {
			throw new RoomTypeException("Room type with id"+roomTypeId+"not found");
		}

	}

}
