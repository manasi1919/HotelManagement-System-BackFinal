package com.hms.controller;

import java.util.List;
import java.util.stream.Collector;

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
import com.hms.dto.ResponseMessage;
import com.hms.dto.RoomRequestDTO;
import com.hms.dto.RoomResponseDTO;
import com.hms.dto.SuccessResponse;
import com.hms.execption.AmenityNotFoundException;
import com.hms.execption.RoomException;
import com.hms.model.Room;
import com.hms.services.RoomServiceIntf;

import io.swagger.v3.oas.annotations.Operation;
@CrossOrigin(origins="http://localhost:3000")
@RestController
@RequestMapping("/api")
public class RoomController {

    @Autowired
    RoomServiceIntf roomServices;

    // Get all rooms
  //retrive all rooms
  	@Operation(summary="Get all rooms ")
  	@GetMapping
  	public ResponseEntity<List<RoomResponseDTO>> findAll()
  	{
  		List<RoomResponseDTO> rooms=roomServices.findAllRooms();
  		if(rooms.size()>0) {
  			return ResponseEntity.ok(rooms);
  		}
  		else {
  			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  		}
  	}

    // Get room by roomId
  	@Operation(summary = "Get a room by ID")
  	@GetMapping("/room/{roomId}")
  	public ResponseEntity<?> findById(@PathVariable(name = "roomId") int roomId) {
  	    try {
  	        RoomResponseDTO room = roomServices.findRoomById(roomId);
  	        if (room != null) {
  	            return new ResponseEntity<>(room, HttpStatus.OK);
  	        } else {
  	            ErrorResponse errorResponse = new ErrorResponse("NOT_FOUND", "Room not found");
  	            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
  	        }
  	    } catch (RoomException ex) {
  	        ErrorResponse errorResponse = new ErrorResponse("BAD_REQUEST", "Error retrieving room: " + ex.getMessage());
  	        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  	    }
  	}

    // Create a new room
    @Operation(summary = "Create a new room")
    @PostMapping("/rooms/post")
    public ResponseEntity<ResponseMessage> newRoom(@RequestBody RoomRequestDTO roomRequestDto) {
        try {
            RoomResponseDTO room = roomServices.newRoom(roomRequestDto);
            if (room != null) {
                return new ResponseEntity<>(new ResponseMessage("POSTSUCCESS", "Room added successfully"), HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(new ResponseMessage("ADDFAILS", "Room already exists"), HttpStatus.BAD_REQUEST);
            }
        } catch (RoomException ex) {
            return new ResponseEntity<>(new ResponseMessage("ADDFAILS", ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    // Update room details
    @Operation(summary = "Update room details")
    @PutMapping("/room/update/{roomId}")
    public ResponseEntity<ResponseMessage> updateById(@PathVariable(name = "roomId") int roomId, @RequestBody RoomRequestDTO roomRequestDto) {
        try {
            RoomResponseDTO room = roomServices.findRoomById(roomId);
            if (room != null) {
                room = roomServices.updateById(roomId, roomRequestDto);
                return new ResponseEntity<>(new ResponseMessage("UPDATESUCCESS", "Room updated successfully"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ResponseMessage("UPDATEFAIL", "Room not found"), HttpStatus.NOT_FOUND);
            }
        } catch (RoomException ex) {
            return new ResponseEntity<>(new ResponseMessage("UPDATEFAIL", ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    // Available rooms of a given roomType
    @Operation(summary = "Retrieve available rooms of a specific type")
    @GetMapping("/rooms/available/{roomTypeId}")
    public ResponseEntity<?> findAvailableRoomsByType(@PathVariable(name = "roomTypeId") int roomTypeId) {
        try {
            List<RoomResponseDTO> rooms = roomServices.findByAvailableByRoomTypeId(true, roomTypeId);
            if (rooms != null && !rooms.isEmpty()) {
                return new ResponseEntity<>(rooms, HttpStatus.OK);
            } else {
                ErrorResponse errorResponse = new ErrorResponse("NOT_FOUND", "No available rooms found for the specified type");
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }
        } catch (RoomException ex) {
            ErrorResponse errorResponse = new ErrorResponse("BAD_REQUEST", "Error retrieving rooms: " + ex.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    // Delete room by ID
    @Operation(summary = "Delete room by ID")
    @DeleteMapping("/rooms/{roomId}")
    public ResponseEntity<ResponseMessage> deleteById(@PathVariable(name = "roomId") int roomId) {
        try {
            RoomResponseDTO room = roomServices.findRoomById(roomId);
            if (room != null) {
                roomServices.deleteById(roomId);
                return new ResponseEntity<>(new ResponseMessage("DELETESUCCESS", "Room deleted successfully"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ResponseMessage("DELETEFAIL", "Room not found"), HttpStatus.NOT_FOUND);
            }
        } catch (RoomException ex) {
            return new ResponseEntity<>(new ResponseMessage("DELETEFAIL", ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    // Retrieve rooms by specific amenity ID
    @Operation(summary = "Retrieve rooms with a specific amenity")
    @GetMapping("/rooms/amenities/{amenityId}")
    public ResponseEntity<?> findRoomsByAmenityId(@PathVariable(name = "amenityId") int amenityId) {
        try {
            List<RoomResponseDTO> rooms = roomServices.findByAmenities_AmenityId(amenityId);
            if (rooms != null && !rooms.isEmpty()) {
                SuccessResponse successResponse = new SuccessResponse("SUCCESS", "Rooms retrieved successfully", rooms);
                return new ResponseEntity<>(successResponse, HttpStatus.OK);
            } else {
                ErrorResponse errorResponse = new ErrorResponse("NOT_FOUND", "No rooms found with the specified amenity");
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }
        } catch (RoomException ex) {
            ErrorResponse errorResponse = new ErrorResponse("BAD_REQUEST", "Error retrieving rooms: " + ex.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    // Retrieve rooms by location
    @Operation(summary = "Retrieve rooms in a specific location")
    @GetMapping("/rooms/location/{location}")
    public ResponseEntity<?> findRoomsByLocation(@PathVariable(name = "location") String location) {
        try {
            List<RoomResponseDTO> rooms = roomServices.findByHotels_Location(location);
            if (rooms != null && !rooms.isEmpty()) {
                return new ResponseEntity<>(rooms, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ErrorResponse("NOT_FOUND", "No rooms found in the specified location"), HttpStatus.NOT_FOUND);
            }
        } catch (RoomException ex) {
            return new ResponseEntity<>(new ErrorResponse("BAD_REQUEST", "Error retrieving rooms: " + ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
    
    @Operation(summary = "Get all rooms that provide a specific amenity")
    @GetMapping("/rooms/{amenity_id}")
    public ResponseEntity<?> findByAmenityId(@PathVariable(name="amenity_id") Integer amenity_id) {
        try {
            List<RoomResponseDTO> rooms = roomServices.findByAmenities_AmenityId(amenity_id);
            if (!rooms.isEmpty()) {
                SuccessResponse successResponse = new SuccessResponse("SUCCESS", "Rooms retrieved successfully", rooms);
                return new ResponseEntity<>(successResponse, HttpStatus.OK);
            } else {
                ErrorResponse errorResponse = new ErrorResponse("NOT_FOUND", "No rooms found with the specified amenity");
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }
        } catch (AmenityNotFoundException ex) {
            ErrorResponse errorResponse = new ErrorResponse("NOT_FOUND", "Amenity not found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        } catch (RoomException ex) {
            ErrorResponse errorResponse = new ErrorResponse("BAD_REQUEST", "Error retrieving rooms: " + ex.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

}
