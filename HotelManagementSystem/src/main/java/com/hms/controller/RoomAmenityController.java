package com.hms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hms.dto.RoomAmenityRequestDTO;
import com.hms.services.RoomAmenityServiceImpl;
@CrossOrigin(origins="http://localhost:3000")
@RestController
@RequestMapping("/api/roomamenity")
public class RoomAmenityController {

	@Autowired
    private RoomAmenityServiceImpl roomAmenityService;
    @PostMapping("/post")
    public ResponseEntity<String> associateAmenitiesToRoom(@RequestBody RoomAmenityRequestDTO roomAmenityDTO) {
        try {
            roomAmenityService.associateAmenitiesToRoom(roomAmenityDTO);
            return new ResponseEntity<>("Amenities successfully added to the room", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
