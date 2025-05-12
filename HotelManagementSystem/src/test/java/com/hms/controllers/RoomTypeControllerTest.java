package com.hms.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.hms.controller.RoomTypeController;
import com.hms.dto.RoomTypeRequestDTO;
import com.hms.dto.RoomTypeResponseDTO;
import com.hms.services.RoomTypeServicIntf;

import static org.junit.jupiter.api.Assertions.*;

class RoomTypeControllerTest {

    private MockMvc mockMvc;

    private RoomTypeController roomTypeController;

    private RoomTypeServicIntf roomTypeService;

    private ModelMapper modelMapper;

    private RoomTypeRequestDTO roomTypeRequestDTO;
    private RoomTypeResponseDTO roomTypeResponseDTO;

    @BeforeEach
    void setUp() throws Exception {
        roomTypeService = mock(RoomTypeServicIntf.class);
        modelMapper = mock(ModelMapper.class);
        roomTypeController = new RoomTypeController();

        // Use reflection to set the private fields
        java.lang.reflect.Field serviceField = roomTypeController.getClass().getDeclaredField("serviceIntf");
        serviceField.setAccessible(true);
        serviceField.set(roomTypeController, roomTypeService);

        java.lang.reflect.Field modelMapperField = roomTypeController.getClass().getDeclaredField("modelMapper");
        modelMapperField.setAccessible(true);
        modelMapperField.set(roomTypeController, modelMapper);

        mockMvc = MockMvcBuilders.standaloneSetup(roomTypeController).build();

        roomTypeRequestDTO = new RoomTypeRequestDTO(1, "Deluxe", "Spacious room with a great view", 150.00, 3);
        roomTypeResponseDTO = new RoomTypeResponseDTO(1, "Deluxe", "Spacious room with a great view", 150.00, 3);
    }

    @Test
    void testFindAll() {
        when(roomTypeService.findAllRoomType()).thenReturn(Arrays.asList(roomTypeResponseDTO));

        ResponseEntity<List<RoomTypeResponseDTO>> response = roomTypeController.findAll();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testNewRoomType() {
        when(roomTypeService.newRoomType(any(RoomTypeRequestDTO.class))).thenReturn(roomTypeResponseDTO);

        ResponseEntity<?> response = roomTypeController.newRoomType(roomTypeRequestDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(roomTypeResponseDTO, response.getBody());
    }

    @Test
    void testNewRoomTypeFailed() {
        when(roomTypeService.newRoomType(any(RoomTypeRequestDTO.class))).thenReturn(null);

        ResponseEntity<?> response = roomTypeController.newRoomType(roomTypeRequestDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testFindRoomTypeById() {
        when(roomTypeService.findByRoomTypeId(anyInt())).thenReturn(roomTypeResponseDTO);

        ResponseEntity<?> response = roomTypeController.findRoomTypeById(1);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(roomTypeResponseDTO, response.getBody());
    }

    @Test
    void testFindRoomTypeByIdNotFound() {
        when(roomTypeService.findByRoomTypeId(anyInt())).thenReturn(null);

        ResponseEntity<?> response = roomTypeController.findRoomTypeById(1);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testUpdateRoomType() {
        when(roomTypeService.findByRoomTypeId(anyInt())).thenReturn(roomTypeResponseDTO);
        when(roomTypeService.updateByRoomTypeId(anyInt(), any(RoomTypeRequestDTO.class))).thenReturn(roomTypeResponseDTO);

        ResponseEntity<RoomTypeResponseDTO> response = roomTypeController.updateRoomType(1, roomTypeRequestDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(roomTypeResponseDTO, response.getBody());
    }

    @Test
    void testUpdateRoomTypeNotFound() {
        when(roomTypeService.findByRoomTypeId(anyInt())).thenReturn(null);

        ResponseEntity<RoomTypeResponseDTO> response = roomTypeController.updateRoomType(1, roomTypeRequestDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeleteRoomType() {
        when(roomTypeService.findByRoomTypeId(anyInt())).thenReturn(roomTypeResponseDTO);

        ResponseEntity<Boolean> response = roomTypeController.deleteByRoomType(1);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
    }

    @Test
    void testDeleteRoomTypeNotFound() {
        when(roomTypeService.findByRoomTypeId(anyInt())).thenReturn(null);

        ResponseEntity<Boolean> response = roomTypeController.deleteByRoomType(1);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertFalse(response.getBody());
    }
}
