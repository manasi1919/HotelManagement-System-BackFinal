package com.hms.controller;
 
import java.util.List;
 

import java.util.Map;
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
 
import com.hms.dto.HotelRequestDTO;
import com.hms.dto.HotelResponseDTO;
import com.hms.services.HotelServiceIntf;
 
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
 
@CrossOrigin(origins="http://localhost:3000")
@RestController
@RequestMapping("/api/hotels")
public class HotelController {
    @Autowired
    private HotelServiceIntf hotelServiceIntf;
    @Operation(summary = "Create a new hotel")
    @PostMapping("/post")
    public ResponseEntity<?> addHotel(@Valid @RequestBody HotelRequestDTO hotelRequestDTO) {
        HotelResponseDTO response = hotelServiceIntf.saveHotel(hotelRequestDTO);
        if (response != null) {
            return new ResponseEntity<>(
                Map.of("code", "POSTSUCCESS", "message", "Hotel added successfully", "data", response),
                HttpStatus.CREATED
            );
        } else {
            return new ResponseEntity<>(
                Map.of("code", "ADDFAILS", "message", "Hotel already exists"),
                HttpStatus.BAD_REQUEST
            );
        }
    }
	/*
	 * @Operation(summary = "Get a list of all hotels")
	 * 
	 * @GetMapping("/all") public ResponseEntity<?> findAllHotels() {
	 * List<HotelResponseDTO> hotels = hotelServiceIntf.findAllHotels(); if
	 * (hotels.isEmpty()) { return new ResponseEntity<>( Map.of("code", "GETFAILS",
	 * "message", "Hotel list is empty"), HttpStatus.NOT_FOUND ); } return new
	 * ResponseEntity<>(Map.of("data", hotels), HttpStatus.OK); }
	 */

    @Operation(summary = "Get a list of all hotels")
    @GetMapping("/all")
    public ResponseEntity<List<HotelResponseDTO>> findAllHotels() {
        List<HotelResponseDTO> hotels = hotelServiceIntf.findAllHotels();
        if (hotels.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(hotels, HttpStatus.OK);
    }
    @Operation(summary = "Get hotel by ID")
    @GetMapping("/{hotel_id}")
    public ResponseEntity<?> findHotelById(@PathVariable(name = "hotel_id") int hotelId) {
        HotelResponseDTO response = hotelServiceIntf.findHotelById(hotelId);
        if (response != null) {
            return new ResponseEntity<>(Map.of("data", response), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                Map.of("code", "GETFAILS", "message", "Hotel doesn't exist"),
                HttpStatus.NOT_FOUND
            );
        }
    }
    @Operation(summary = "Get list of hotels providing a specific amenity")
    @GetMapping("/amenity/{amenity_id}")
    public ResponseEntity<?> findHotelsByAmenityId(@PathVariable(name = "amenity_id") Integer amenityId) {
        List<HotelResponseDTO> hotels = (List<HotelResponseDTO>) hotelServiceIntf.findHotelsByAmenityId(amenityId);
        if (hotels.isEmpty()) {
            return new ResponseEntity<>(
                Map.of("code", "GETFAILS", "message", "No hotel is found with the specific amenity"),
                HttpStatus.NOT_FOUND
            );
        }
        return new ResponseEntity<>(Map.of("data", hotels), HttpStatus.OK);
    }
    @Operation(summary = "Update hotel details")
    @PutMapping("/update/{hotel_id}")
    public ResponseEntity<?> updateHotel(
        @PathVariable(name = "hotel_id") int hotelId,
        @Valid @RequestBody HotelRequestDTO hotelRequestDTO
    ) {
        HotelResponseDTO response = hotelServiceIntf.updateHotel(hotelId, hotelRequestDTO);
        if (response != null) {
            return new ResponseEntity<>(
                Map.of("code", "UPDATESUCCESS", "message", "Hotel updated successfully", "data", response),
                HttpStatus.OK
            );
        } else {
            return new ResponseEntity<>(
                Map.of("code", "UPDTFAILS", "message", "Hotel doesn't exist"),
                HttpStatus.NOT_FOUND
            );
        }
    }
    @Operation(summary = "Delete a hotel")
    @DeleteMapping("/delete/{hotel_id}")
    public ResponseEntity<?> deleteHotelById(@PathVariable(name = "hotel_id") int hotelId) {
        boolean deleted = hotelServiceIntf.deleteHotelById(hotelId);
        if (deleted) {
            return new ResponseEntity<>(
                Map.of("code", "DELETESUCCESS", "message", "Hotel deleted successfully"),
                HttpStatus.OK
            );
        } else {
            return new ResponseEntity<>(
                Map.of("code", "DLTFAILS", "message", "Hotel doesn't exist"),
                HttpStatus.NOT_FOUND
            );
        }
    }
}