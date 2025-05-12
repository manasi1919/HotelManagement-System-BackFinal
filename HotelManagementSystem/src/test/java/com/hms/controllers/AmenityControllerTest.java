package com.hms.controllers;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hms.controller.AmenityController;
import com.hms.dto.AmenityRequestDTO;
import com.hms.dto.AmenityResponseDTO;
import com.hms.execption.AmenityNotFoundException;
import com.hms.execption.ResourceNotFoundException;
import com.hms.repository.AmenityRepository;
import com.hms.services.AmenityServiceIntf;

@ExtendWith(MockitoExtension.class)
class AmenityControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AmenityServiceIntf amenityService;

    @Mock
    private AmenityRepository amenityRepository;

    @InjectMocks
    private AmenityController amenityController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(amenityController).build();
    }

    @Test
    void testCreateAmenity() throws Exception {
        AmenityRequestDTO requestDTO = new AmenityRequestDTO("WiFi", "High-speed internet");
        AmenityResponseDTO responseDTO = new AmenityResponseDTO(1, "WiFi", "High-speed internet", Collections.emptyList());

        when(amenityService.addAmenity(any(AmenityRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/amenity")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.amenityId").value(1))
                .andExpect(jsonPath("$.name").value("WiFi"));
    }

    @Test
    void testGetAllAmenities() throws Exception {
        List<AmenityResponseDTO> amenities = Arrays.asList(
            new AmenityResponseDTO(1, "WiFi", "High-speed internet", Collections.emptyList()),
            new AmenityResponseDTO(2, "Pool", "Swimming pool", Collections.emptyList())
        );

        when(amenityService.getAllAmenities()).thenReturn(amenities);

        mockMvc.perform(get("/api/amenity/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].amenityId").value(1))
                .andExpect(jsonPath("$[1].name").value("Pool"));
    }

    @Test
    void testGetAmenityById() throws Exception {
        AmenityResponseDTO responseDTO = new AmenityResponseDTO(1, "WiFi", "High-speed internet", Collections.emptyList());

        when(amenityService.findById(1)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/amenity/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amenityId").value(1))
                .andExpect(jsonPath("$.name").value("WiFi"));
    }

    @Test
    void testUpdateAmenity() throws Exception {
        AmenityRequestDTO requestDTO = new AmenityRequestDTO("Gym", "Fully equipped gym");
        AmenityResponseDTO responseDTO = new AmenityResponseDTO(1, "Gym", "Fully equipped gym", Collections.emptyList());

        when(amenityService.updateAmenity(eq(1), any(AmenityRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/amenity/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Gym"));
    }

    @Test
    void testDeleteAmenity() throws Exception {
        when(amenityService.deleteAmenityById(1)).thenReturn(true);

        mockMvc.perform(delete("/api/amenity/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetAmenitiesByRoomId() throws Exception {
        when(amenityRepository.existsById(1)).thenReturn(true);
        List<AmenityResponseDTO> amenities = Arrays.asList(
            new AmenityResponseDTO(1, "WiFi", "High-speed internet", Collections.emptyList())
        );
        when(amenityService.getRoomById(1)).thenReturn(amenities);

        mockMvc.perform(get("/api/amenity/room/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].amenityId").value(1));
    }

    @Test
    void testGetAmenitiesByHotelId() throws Exception {
        when(amenityRepository.existsById(1)).thenReturn(true);
        List<AmenityResponseDTO> amenities = Arrays.asList(
            new AmenityResponseDTO(1, "WiFi", "High-speed internet", Collections.emptyList())
        );
        when(amenityService.getHotelById(1)).thenReturn(amenities);

        mockMvc.perform(get("/api/amenity/hotel/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].amenityId").value(1));
    } 

}
