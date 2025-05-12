package com.hms.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hms.dto.ErrorResponse;
import com.hms.dto.RoomTypeRequestDTO;
import com.hms.dto.RoomTypeResponseDTO;
import com.hms.services.RoomTypeServicIntf;

import io.swagger.v3.oas.annotations.Operation;
@CrossOrigin(origins="http://localhost:3000")
@RestController
@RequestMapping("api/roomType")
public class RoomTypeController
{
	@Autowired
	RoomTypeServicIntf serviceIntf;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Operation(summary="Create a new roomType")
	@GetMapping("/all")
	public ResponseEntity<List<RoomTypeResponseDTO>> findAll()
	{
		List<RoomTypeResponseDTO> roomTypes=serviceIntf.findAllRoomType();
		if(roomTypes.size()>0)
		{
			return new ResponseEntity<>(roomTypes,HttpStatus.OK);
		}
		else 
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@Operation(summary = "Create a new room type")
	@PostMapping("/post")
	public ResponseEntity<?> newRoomType(@RequestBody RoomTypeRequestDTO roomTypeRequestDTO) {
	    RoomTypeResponseDTO roomTypes = serviceIntf.newRoomType(roomTypeRequestDTO);
	    if (roomTypes != null) {
	        return new ResponseEntity<>(roomTypes, HttpStatus.CREATED);
	    } else {
	        return new ResponseEntity<>(new ErrorResponse("Failed to create room type", "BAD_REQUEST"), HttpStatus.BAD_REQUEST);
	    }
	}
	@Operation(summary = "Get a Room Type by Id")
	@GetMapping("/{roomTypeId}")
	public ResponseEntity<?> findRoomTypeById(@PathVariable(name = "roomTypeId") int roomTypeId) {
	    RoomTypeResponseDTO roomTypes = serviceIntf.findByRoomTypeId(roomTypeId);
	    if (roomTypes != null) {
	        return new ResponseEntity<>(roomTypes, HttpStatus.OK);
	    } else {
	        return new ResponseEntity<>(new ErrorResponse("Room type not found", "NOT_FOUND"), HttpStatus.NOT_FOUND);
	    }
	}

	@Operation(summary="Update RoomType details.")
	 @PutMapping("/update/{roomTypeId}")
	    public ResponseEntity<RoomTypeResponseDTO> updateRoomType(@PathVariable(name = "roomTypeId") int roomTypeId, @RequestBody RoomTypeRequestDTO roomTypeRequestDTO) {
	        RoomTypeResponseDTO roomTypes = serviceIntf.findByRoomTypeId(roomTypeId);
	        if (roomTypes != null) {
	            roomTypes = serviceIntf.updateByRoomTypeId(roomTypeId, roomTypeRequestDTO);
	            return new ResponseEntity<>(roomTypes, HttpStatus.OK);
	        } else {
	            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	        }
	    }
	@DeleteMapping("/delete/{roomTypeId}")
	public ResponseEntity<Boolean> deleteByRoomType(@PathVariable(name="roomTypeId") int roomTypeId){
		RoomTypeResponseDTO roomTypes=serviceIntf.findByRoomTypeId(roomTypeId);
		if(roomTypes!=null) {
			serviceIntf.deleteByRoomTypeId(roomTypeId);
			return new ResponseEntity<>(true,HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>(false,HttpStatus.NOT_FOUND);
		}
	}
	

}
