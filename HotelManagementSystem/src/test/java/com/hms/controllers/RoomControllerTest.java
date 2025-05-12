package com.hms.controllers;

import com.hms.controller.RoomController;
import com.hms.dto.*;
import com.hms.model.Room;
import com.hms.services.RoomServiceIntf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RoomControllerTest {

    @Mock
    private RoomServiceIntf roomServices;

    @InjectMocks
    private RoomController roomController;

    private RoomRequestDTO roomRequestDTO;
    private RoomResponseDTO roomResponseDTO;
    
    @BeforeEach
    void setUp() {
        roomRequestDTO = new RoomRequestDTO(1, 101, true, 1);
        roomResponseDTO = new RoomResponseDTO(1, 101, 1, true, null);
    }

    @Test
    void testFindAllRooms() {
        when(roomServices.findAllRooms()).thenReturn(List.of(roomResponseDTO));
        ResponseEntity<List<RoomResponseDTO>> response = roomController.findAll();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testFindAllRooms_Empty() {
        when(roomServices.findAllRooms()).thenReturn(Collections.emptyList());
        ResponseEntity<List<RoomResponseDTO>> response = roomController.findAll();
       // assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testFindRoomById() {
        when(roomServices.findRoomById(1)).thenReturn(roomResponseDTO);
        ResponseEntity<?> response = roomController.findById(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testFindRoomById_NotFound() {
        when(roomServices.findRoomById(1)).thenReturn(null);
        ResponseEntity<?> response = roomController.findById(1);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testCreateRoom() {
        when(roomServices.newRoom(roomRequestDTO)).thenReturn(roomResponseDTO);
        ResponseEntity<ResponseMessage> response = roomController.newRoom(roomRequestDTO);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void testUpdateRoom() {
        when(roomServices.findRoomById(1)).thenReturn(roomResponseDTO);
        when(roomServices.updateById(1, roomRequestDTO)).thenReturn(roomResponseDTO);
        ResponseEntity<ResponseMessage> response = roomController.updateById(1, roomRequestDTO);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testDeleteRoom() {
        when(roomServices.findRoomById(1)).thenReturn(roomResponseDTO);
        ResponseEntity<ResponseMessage> response = roomController.deleteById(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testFindAvailableRoomsByType() {
        when(roomServices.findByAvailableByRoomTypeId(true, 1)).thenReturn(List.of(roomResponseDTO));
        ResponseEntity<?> response = roomController.findAvailableRoomsByType(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testFindRoomsByAmenityId() {
        when(roomServices.findByAmenities_AmenityId(1)).thenReturn(List.of(roomResponseDTO));
        ResponseEntity<?> response = roomController.findRoomsByAmenityId(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testFindRoomsByLocation() {
        when(roomServices.findByHotels_Location("New York")).thenReturn(List.of(roomResponseDTO));
        ResponseEntity<?> response = roomController.findRoomsByLocation("New York");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
