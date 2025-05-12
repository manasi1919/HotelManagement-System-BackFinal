package com.hms.controller;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.hms.dto.AmenityRequestDTO;
import com.hms.dto.AmenityResponseDTO;
import com.hms.execption.AmenityNotFoundException;
import com.hms.execption.ResourceNotFoundException;
import com.hms.model.Amenity;
import com.hms.repository.AmenityRepository;
import com.hms.services.AmenityServiceIntf;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins="http://localhost:3000")
@Tag(name = "Amenity Management", description = "Endpoints for managing amenities")
@RestController
@RequestMapping("/api/amenity")
public class AmenityController {
    @Autowired
    private AmenityServiceIntf amenityService;

    @Autowired
    private AmenityRepository amenityRepository;

    @Operation(summary = "Create a new Amenity", description = "This API creates a new amenity")
    @PostMapping
    public ResponseEntity<AmenityResponseDTO> createAmenity(@RequestBody AmenityRequestDTO amenityRequestDTO) {
        AmenityResponseDTO amenityResponseDTO = amenityService.addAmenity(amenityRequestDTO);
        return new ResponseEntity<>(amenityResponseDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all Amenities", description = "This API returns all available amenities")
    @GetMapping("/all")
    public ResponseEntity<List<AmenityResponseDTO>> getAllAmenities() {
        List<AmenityResponseDTO> amenities = amenityService.getAllAmenities();
        return new ResponseEntity<>(amenities, HttpStatus.OK);
    }

    @Operation(summary = "Get Amenity by ID", description = "Fetches an Amenity by its ID")
    @GetMapping("/{amenityId}")
    public ResponseEntity<AmenityResponseDTO> getAmenityById(@PathVariable("amenityId") int amenityId) {
        AmenityResponseDTO amenity = amenityService.findById(amenityId);
        return new ResponseEntity<>(amenity, HttpStatus.OK);
    }

    @Operation(summary = "Update Amenity", description = "This API updates an existing amenity")
    @PutMapping("/{amenityId}")
    public ResponseEntity<AmenityResponseDTO> updateAmenity(
            @PathVariable("amenityId") int amenityId,
            @RequestBody AmenityRequestDTO amenityRequestDTO) {
        try {
            AmenityResponseDTO updatedAmenity = amenityService.updateAmenity(amenityId, amenityRequestDTO);
            return new ResponseEntity<>(updatedAmenity, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Delete Amenity by ID", description = "This API deletes an amenity by its ID")
    @DeleteMapping("/{amenityId}")
    public ResponseEntity<Void> deleteAmenity(@PathVariable("amenityId") int amenityId) {
        boolean isDeleted = amenityService.deleteAmenityById(amenityId);
        return isDeleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Get Amenities by Room ID", description = "Fetches amenities for a specific room")
    @GetMapping("/room/{roomId}")
    public ResponseEntity<?> getRoomById(@PathVariable("roomId") int roomId) {
        if (amenityRepository.existsById(roomId)) {
            List<AmenityResponseDTO> roomById = amenityService.getRoomById(roomId);
            return ResponseEntity.ok(roomById.isEmpty() ? Collections.emptyList() : roomById);
        } else {
            throw new AmenityNotFoundException("Room not found with given room ID");
        }
    }

    @Operation(summary = "Get Amenities by Hotel ID", description = "Fetches amenities for a specific hotel")
    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<?> getHotelById(@PathVariable("hotelId") int hotelId) {
        if (amenityRepository.existsById(hotelId)) {
            List<AmenityResponseDTO> hotelById = amenityService.getHotelById(hotelId);
            return ResponseEntity.ok(hotelById);
        } else {
            throw new AmenityNotFoundException("Hotel not found with given hotel ID");
        }
    }
}
