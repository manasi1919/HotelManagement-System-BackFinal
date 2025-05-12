package com.hms.controllers;

import com.hms.controller.HotelController;
import com.hms.dto.HotelRequestDTO;
import com.hms.dto.HotelResponseDTO;
import com.hms.services.HotelServiceIntf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class HotelControllerTest {

    @InjectMocks
    private HotelController hotelController;

    @Mock
    private HotelServiceIntf hotelServiceIntf;

    private HotelRequestDTO hotelRequestDTO;
    private HotelResponseDTO hotelResponseDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mock objects

        // Create test data for HotelRequestDTO
        hotelRequestDTO = new HotelRequestDTO();
        hotelRequestDTO.setName("Grand Hotel");
        hotelRequestDTO.setLocation("New York");
        hotelRequestDTO.setDescription("Luxury hotel in the heart of the city");

        // Create test data for HotelResponseDTO
        hotelResponseDTO = new HotelResponseDTO(1, "Grand Hotel", "New York", "Luxury hotel in the heart of the city", null);
    }

    @Test
    public void testAddHotel_Success() {
        // Arrange
        when(hotelServiceIntf.saveHotel(hotelRequestDTO)).thenReturn(hotelResponseDTO);

        // Act
        ResponseEntity<?> response = hotelController.addHotel(hotelRequestDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Hotel added successfully"));
        verify(hotelServiceIntf, times(1)).saveHotel(hotelRequestDTO);
    }

    @Test
    public void testAddHotel_Failure() {
        // Arrange
        when(hotelServiceIntf.saveHotel(hotelRequestDTO)).thenReturn(null);

        // Act
        ResponseEntity<?> response = hotelController.addHotel(hotelRequestDTO);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Hotel already exists"));
        verify(hotelServiceIntf, times(1)).saveHotel(hotelRequestDTO);
    }

    @Test
    public void testFindAllHotels_Success() {
        // Arrange
        List<HotelResponseDTO> hotelList = Arrays.asList(hotelResponseDTO);
        when(hotelServiceIntf.findAllHotels()).thenReturn(hotelList);

        // Act
        ResponseEntity<List<HotelResponseDTO>> response = hotelController.findAllHotels();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
        verify(hotelServiceIntf, times(1)).findAllHotels();
    }

    @Test
    public void testFindAllHotels_Empty() {
        // Arrange
        List<HotelResponseDTO> hotelList = Arrays.asList();
        when(hotelServiceIntf.findAllHotels()).thenReturn(hotelList);

        // Act
        ResponseEntity<List<HotelResponseDTO>> response = hotelController.findAllHotels();

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(hotelServiceIntf, times(1)).findAllHotels();
    }

    @Test
    public void testFindHotelById_Success() {
        // Arrange
        when(hotelServiceIntf.findHotelById(1)).thenReturn(hotelResponseDTO);

        // Act
        ResponseEntity<?> response = hotelController.findHotelById(1);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Grand Hotel"));
        verify(hotelServiceIntf, times(1)).findHotelById(1);
    }

    @Test
    public void testFindHotelById_Failure() {
        // Arrange
        when(hotelServiceIntf.findHotelById(1)).thenReturn(null);

        // Act
        ResponseEntity<?> response = hotelController.findHotelById(1);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Hotel doesn't exist"));
        verify(hotelServiceIntf, times(1)).findHotelById(1);
    }

    @Test
    public void testUpdateHotel_Success() {
        // Arrange
        when(hotelServiceIntf.updateHotel(1, hotelRequestDTO)).thenReturn(hotelResponseDTO);

        // Act
        ResponseEntity<?> response = hotelController.updateHotel(1, hotelRequestDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Hotel updated successfully"));
        verify(hotelServiceIntf, times(1)).updateHotel(1, hotelRequestDTO);
    }

    @Test
    public void testUpdateHotel_Failure() {
        // Arrange
        when(hotelServiceIntf.updateHotel(1, hotelRequestDTO)).thenReturn(null);

        // Act
        ResponseEntity<?> response = hotelController.updateHotel(1, hotelRequestDTO);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Hotel doesn't exist"));
        verify(hotelServiceIntf, times(1)).updateHotel(1, hotelRequestDTO);
    }

    @Test
    public void testDeleteHotel_Success() {
        // Arrange
        when(hotelServiceIntf.deleteHotelById(1)).thenReturn(true);

        // Act
        ResponseEntity<?> response = hotelController.deleteHotelById(1);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Hotel deleted successfully"));
        verify(hotelServiceIntf, times(1)).deleteHotelById(1);
    }

    @Test
    public void testDeleteHotel_Failure() {
        // Arrange
        when(hotelServiceIntf.deleteHotelById(1)).thenReturn(false);

        // Act
        ResponseEntity<?> response = hotelController.deleteHotelById(1);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Hotel doesn't exist"));
        verify(hotelServiceIntf, times(1)).deleteHotelById(1);
    }
}
