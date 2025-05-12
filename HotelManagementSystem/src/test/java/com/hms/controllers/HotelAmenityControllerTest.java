package com.hms.controllers;

import com.hms.controller.HotelAmenityController;
import com.hms.dto.HotelAmenityRequestDTO;
import com.hms.services.HotelAmenityServicesImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class) // Ensure proper Mockito integration
public class HotelAmenityControllerTest {

    @InjectMocks
    private HotelAmenityController hotelAmenityController;

    @Mock
    private HotelAmenityServicesImpl hotelAmenityService;

    private HotelAmenityRequestDTO hotelAmenityRequestDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        hotelAmenityRequestDTO = new HotelAmenityRequestDTO();
        hotelAmenityRequestDTO.setHotelId(1);
        hotelAmenityRequestDTO.setAmenityId(List.of(1, 2, 3));
    }
    @Test
    public void testAssociateAmenitiesToHotel_Success() {
        // Act
        ResponseEntity<String> response = hotelAmenityController.associateAmenitiesToHotel(hotelAmenityRequestDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Amenities successfully added to the hotel", response.getBody());

        // Verify method was called
        verify(hotelAmenityService, times(1)).associateAmenitiesToHotel(hotelAmenityRequestDTO);
    }

    @Test
    public void testAssociateAmenitiesToHotel_Failure() {
        // Arrange
        doThrow(new RuntimeException("Error while associating amenities"))
            .when(hotelAmenityService).associateAmenitiesToHotel(hotelAmenityRequestDTO);

        // Act
        ResponseEntity<String> response = hotelAmenityController.associateAmenitiesToHotel(hotelAmenityRequestDTO);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error: Error while associating amenities", response.getBody());

        // Verify method was called once
        verify(hotelAmenityService, times(1)).associateAmenitiesToHotel(hotelAmenityRequestDTO);
    }
}
