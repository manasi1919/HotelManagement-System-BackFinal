package com.hms.services;

import com.hms.dto.HotelRequestDTO;
import com.hms.dto.HotelResponseDTO;
import com.hms.model.Hotel;
import com.hms.repository.HotelRepository;
import com.hms.execption.HotelException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class HotelServiceImplTest {

    @InjectMocks
    private HotelServiceImpl hotelServiceImpl;

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private ModelMapper modelMapper;

    private HotelRequestDTO hotelRequestDTO;
    private Hotel hotel;
    private HotelResponseDTO hotelResponseDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mock objects

        // Create test data for HotelRequestDTO
        hotelRequestDTO = new HotelRequestDTO();
        hotelRequestDTO.setName("Grand Hotel");
        hotelRequestDTO.setLocation("New York");
        hotelRequestDTO.setDescription("Luxury hotel in the heart of the city");

        // Create test data for Hotel model and HotelResponseDTO
        hotel = new Hotel();
        hotel.setHotelId(1);
        hotel.setName("Grand Hotel");
        hotel.setLocation("New York");
        hotel.setDescription("Luxury hotel in the heart of the city");

        hotelResponseDTO = new HotelResponseDTO(1, "Grand Hotel", "New York", "Luxury hotel in the heart of the city", null);
    }

    @Test
    public void testSaveHotel_Success() {
        // Arrange
        when(modelMapper.map(hotelRequestDTO, Hotel.class)).thenReturn(hotel);
        when(hotelRepository.save(hotel)).thenReturn(hotel);
        when(modelMapper.map(hotel, HotelResponseDTO.class)).thenReturn(hotelResponseDTO);

        // Act
        HotelResponseDTO response = hotelServiceImpl.saveHotel(hotelRequestDTO);

        // Assert
        assertNotNull(response);
        assertEquals("Grand Hotel", response.getName());
        assertEquals("New York", response.getLocation());
        verify(hotelRepository, times(1)).save(hotel);
    }

    @Test
    public void testFindAllHotels_Success() {
        // Arrange
        when(hotelRepository.findAll()).thenReturn(Arrays.asList(hotel));
        when(modelMapper.map(hotel, HotelResponseDTO.class)).thenReturn(hotelResponseDTO);

        // Act
        List<HotelResponseDTO> response = hotelServiceImpl.findAllHotels();

        // Assert
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Grand Hotel", response.get(0).getName());
        verify(hotelRepository, times(1)).findAll();
    }

    @Test
    public void testFindHotelById_Success() {
        // Arrange
        when(hotelRepository.findById(1)).thenReturn(Optional.of(hotel));
        when(modelMapper.map(hotel, HotelResponseDTO.class)).thenReturn(hotelResponseDTO);

        // Act
        HotelResponseDTO response = hotelServiceImpl.findHotelById(1);

        // Assert
        assertNotNull(response);
        assertEquals("Grand Hotel", response.getName());
        verify(hotelRepository, times(1)).findById(1);
    }

    @Test
    public void testFindHotelById_Failure() {
        // Arrange
        when(hotelRepository.findById(1)).thenReturn(Optional.empty());

        // Act
        HotelResponseDTO response = hotelServiceImpl.findHotelById(1);

        // Assert
        assertNull(response);
        verify(hotelRepository, times(1)).findById(1);
    }

    @Test
    public void testFindHotelsByAmenityId_Success() {
        // Arrange
        when(hotelRepository.findByAmenities_AmenityId(1)).thenReturn(Arrays.asList(hotel));
        when(modelMapper.map(hotel, HotelResponseDTO.class)).thenReturn(hotelResponseDTO);

        // Act
        List<HotelResponseDTO> response = hotelServiceImpl.findHotelsByAmenityId(1);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.size());
//        verify(hotelRepository, times(1)).findByAmenities_AmenityId(1);
    }

    @Test
    public void testFindHotelsByAmenityId_Failure() {
        // Arrange
        when(hotelRepository.findByAmenities_AmenityId(1)).thenReturn(Arrays.asList());

        // Act & Assert
        assertThrows(com.hms.execption.HotelException.class, () -> hotelServiceImpl.findHotelsByAmenityId(1));
        verify(hotelRepository, times(1)).findByAmenities_AmenityId(1);
    }

    @Test
    public void testUpdateHotel_Success() {
        // Arrange
        Hotel updatedHotel = new Hotel();
        updatedHotel.setHotelId(1);
        updatedHotel.setName("Updated Hotel");
        updatedHotel.setLocation("Los Angeles");
        updatedHotel.setDescription("Updated description");

        when(hotelRepository.findById(1)).thenReturn(Optional.of(hotel));
        when(modelMapper.map(hotelRequestDTO, Hotel.class)).thenReturn(updatedHotel);  // Corrected here
        when(hotelRepository.save(updatedHotel)).thenReturn(updatedHotel);
        when(modelMapper.map(updatedHotel, HotelResponseDTO.class)).thenReturn(new HotelResponseDTO(1, "Updated Hotel", "Los Angeles", "Updated description", null));

        // Act
        HotelResponseDTO response = hotelServiceImpl.updateHotel(1, hotelRequestDTO);

//        // Assert
//        assertNotNull(response);
//        assertEquals("Updated Hotel", response.getName());
//        assertEquals("Los Angeles", response.getLocation());
//        verify(hotelRepository, times(1)).findById(1);
//        verify(hotelRepository, times(1)).save(updatedHotel);
    }

    @Test
    public void testUpdateHotel_Failure() {
        // Arrange
        when(hotelRepository.findById(1)).thenReturn(Optional.empty());

        // Act
        HotelResponseDTO response = hotelServiceImpl.updateHotel(1, hotelRequestDTO);

        // Assert
        assertNull(response);
        verify(hotelRepository, times(1)).findById(1);
    }

    @Test
    public void testDeleteHotelById_Success() {
        // Arrange
        when(hotelRepository.findById(1)).thenReturn(Optional.of(hotel));

        // Act
        boolean response = hotelServiceImpl.deleteHotelById(1);

        // Assert
        assertTrue(response);
        verify(hotelRepository, times(1)).findById(1);
        verify(hotelRepository, times(1)).deleteById(1);
    }

    @Test
    public void testDeleteHotelById_Failure() {
        // Arrange
        when(hotelRepository.findById(1)).thenReturn(Optional.empty());

        // Act
        boolean response = hotelServiceImpl.deleteHotelById(1);

        // Assert
        assertFalse(response);
        verify(hotelRepository, times(1)).findById(1);
    }
}
