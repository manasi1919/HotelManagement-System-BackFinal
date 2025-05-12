package com.hms.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.hms.dto.AmenityRequestDTO;
import com.hms.dto.AmenityResponseDTO;
import com.hms.execption.AmenityNotFoundException;
import com.hms.execption.ResourceNotFoundException;
import com.hms.model.Amenity;
import com.hms.repository.AmenityRepository;

@ExtendWith(MockitoExtension.class)
class AmenityServiceImplTest {

    @Mock
    private AmenityRepository amenityRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AmenityServiceImpl amenityService;

    private Amenity amenity;
    private AmenityResponseDTO responseDTO;
    private AmenityRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        amenity = new Amenity(1, "WiFi", "High-speed internet", null, null);
        responseDTO = new AmenityResponseDTO(1, "WiFi", "High-speed internet", null);
        requestDTO = new AmenityRequestDTO("WiFi", "High-speed internet");
    }

    @Test
    void testAddAmenity() {
        when(modelMapper.map(any(AmenityRequestDTO.class), eq(Amenity.class))).thenReturn(amenity);
        when(amenityRepository.save(any(Amenity.class))).thenReturn(amenity);
        when(modelMapper.map(any(Amenity.class), eq(AmenityResponseDTO.class))).thenReturn(responseDTO);

        AmenityResponseDTO result = amenityService.addAmenity(requestDTO);

        assertNotNull(result);
        assertEquals("WiFi", result.getName());
        verify(amenityRepository, times(1)).save(any(Amenity.class));
    }

    @Test
    void testFindById() {
        when(amenityRepository.findById(anyInt())).thenReturn(Optional.of(amenity));
        when(modelMapper.map(any(Amenity.class), eq(AmenityResponseDTO.class))).thenReturn(responseDTO);

        AmenityResponseDTO result = amenityService.findById(1);

        assertNotNull(result);
        assertEquals(1, result.getAmenityId());
        verify(amenityRepository, times(1)).findById(1);
    }

    @Test
    void testFindById_NotFound() {
        when(amenityRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> amenityService.findById(1));
        verify(amenityRepository, times(1)).findById(1);
    }



    @Test
    void testDeleteAmenityById_NotFound() {
        when(amenityRepository.existsById(anyInt())).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> amenityService.deleteAmenityById(1));

        verify(amenityRepository, times(1)).existsById(1);
        verify(amenityRepository, never()).deleteById(anyInt());
    }

    @Test
    void testGetAllAmenities() {
        List<Amenity> amenities = Arrays.asList(amenity);

        when(amenityRepository.findAll()).thenReturn(amenities);
        when(modelMapper.map(any(Amenity.class), eq(AmenityResponseDTO.class))).thenReturn(responseDTO);

        List<AmenityResponseDTO> result = amenityService.getAllAmenities();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(amenityRepository, times(1)).findAll();
    }

    @Test
    void testUpdateAmenity() {
        when(amenityRepository.findById(anyInt())).thenReturn(Optional.of(amenity));
        when(amenityRepository.save(any(Amenity.class))).thenReturn(amenity);
        when(modelMapper.map(any(Amenity.class), eq(AmenityResponseDTO.class))).thenReturn(responseDTO);

        AmenityResponseDTO result = amenityService.updateAmenity(1, requestDTO);

        assertNotNull(result);
        assertEquals("WiFi", result.getName());
        verify(amenityRepository, times(1)).findById(1);
        verify(amenityRepository, times(1)).save(any(Amenity.class));
    }

    @Test
    void testUpdateAmenity_NotFound() {
        when(amenityRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> amenityService.updateAmenity(1, requestDTO));

        verify(amenityRepository, times(1)).findById(1);
        verify(amenityRepository, never()).save(any(Amenity.class));
    }

    @Test
    void testGetRoomById() {
        when(amenityRepository.findAmenitiesByRoomId(anyInt())).thenReturn(Arrays.asList(amenity));
        when(modelMapper.map(any(Amenity.class), eq(AmenityResponseDTO.class))).thenReturn(responseDTO);

        List<AmenityResponseDTO> result = amenityService.getRoomById(1);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(amenityRepository, times(1)).findAmenitiesByRoomId(1);
    }

    @Test
    void testGetRoomById_NotFound() {
        when(amenityRepository.findAmenitiesByRoomId(anyInt())).thenReturn(Arrays.asList());

        assertThrows(AmenityNotFoundException.class, () -> amenityService.getRoomById(1));

        verify(amenityRepository, times(1)).findAmenitiesByRoomId(1);
    }

    @Test
    void testGetHotelById() {
        when(amenityRepository.findAmenitiesByHotelId(anyInt())).thenReturn(Arrays.asList(amenity));
        when(modelMapper.map(any(Amenity.class), eq(AmenityResponseDTO.class))).thenReturn(responseDTO);

        List<AmenityResponseDTO> result = amenityService.getHotelById(1);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(amenityRepository, times(1)).findAmenitiesByHotelId(1);
    }

    @Test
    void testGetHotelById_NotFound() {
        when(amenityRepository.findAmenitiesByHotelId(anyInt())).thenReturn(Arrays.asList());

        assertThrows(AmenityNotFoundException.class, () -> amenityService.getHotelById(1));

        verify(amenityRepository, times(1)).findAmenitiesByHotelId(1);
    }
}
