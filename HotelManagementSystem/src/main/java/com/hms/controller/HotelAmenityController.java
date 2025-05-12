package com.hms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hms.dto.HotelAmenityRequestDTO;
import com.hms.services.HotelAmenityServicesImpl;
@CrossOrigin(origins="http://localhost:3000")
@RestController
@RequestMapping("/api/hotelamenity")
public class HotelAmenityController {

	@Autowired
    private HotelAmenityServicesImpl hotelAmenityService;
    @PostMapping("/hotelAmenity/post")
    public ResponseEntity<String> associateAmenitiesToHotel(@RequestBody HotelAmenityRequestDTO hotelAmenityRequestDTO) {
        try {
            hotelAmenityService.associateAmenitiesToHotel(hotelAmenityRequestDTO);
            return new ResponseEntity<>("Amenities successfully added to the hotel", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
