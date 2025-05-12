package com.hms.services;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.hms.dto.RoomTypeRequestDTO;
import com.hms.dto.RoomTypeResponseDTO;
import com.hms.execption.RoomTypeException;
import com.hms.model.RoomType;
import com.hms.repository.RoomTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.mockito.*;

import java.util.Optional;
import java.util.List;
import java.util.Arrays;
import java.lang.reflect.Field;

@SpringBootTest
class RoomTypeServiceImplTest {

    @Mock
    private RoomTypeRepository roomTypeRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private RoomTypeServiceImpl roomTypeService;

    private RoomTypeRequestDTO roomTypeRequestDTO;
    private RoomTypeResponseDTO roomTypeResponseDTO;
    private RoomType roomType;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        roomTypeRequestDTO = new RoomTypeRequestDTO(1, "Deluxe", "Spacious room with a great view", 150.00, 3);
        roomTypeResponseDTO = new RoomTypeResponseDTO(1, "Deluxe", "Spacious room with a great view", 150.00, 3);
        roomType = new RoomType(); // Using the no-argument constructor
        roomType.setTypeName("Deluxe");
        roomType.setDescription("Spacious room with a great view");
        roomType.setPricePerNight(150.00);
        roomType.setMaxOccupancy(3);
    }

    @Test
    void testFindAllRoomType() {
        // Set the roomTypeId using reflection to simulate auto-generation
        setRoomTypeId(roomType, 1);

        when(roomTypeRepository.findAll()).thenReturn(Arrays.asList(roomType));
        when(modelMapper.map(any(RoomType.class), eq(RoomTypeResponseDTO.class))).thenReturn(roomTypeResponseDTO);

        List<RoomTypeResponseDTO> response = roomTypeService.findAllRoomType();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Deluxe", response.get(0).getTypeName());
        verify(roomTypeRepository, times(1)).findAll();
    }

    @Test
    void testNewRoomType() {
        // Set the roomTypeId using reflection to simulate auto-generation
        setRoomTypeId(roomType, 1);

        when(modelMapper.map(any(RoomTypeRequestDTO.class), eq(RoomType.class))).thenReturn(roomType);
        when(roomTypeRepository.save(any(RoomType.class))).thenReturn(roomType);
        when(modelMapper.map(any(RoomType.class), eq(RoomTypeResponseDTO.class))).thenReturn(roomTypeResponseDTO);

        RoomTypeResponseDTO response = roomTypeService.newRoomType(roomTypeRequestDTO);

        assertNotNull(response);
        assertEquals("Deluxe", response.getTypeName());
        verify(roomTypeRepository, times(1)).save(any(RoomType.class));
    }

    @Test
    void testUpdateByRoomTypeId() {
        // Set the roomTypeId using reflection to simulate auto-generation
        setRoomTypeId(roomType, 1);

        when(roomTypeRepository.findById(anyInt())).thenReturn(Optional.of(roomType));
        when(roomTypeRepository.save(any(RoomType.class))).thenReturn(roomType);
        when(modelMapper.map(any(RoomType.class), eq(RoomTypeResponseDTO.class))).thenReturn(roomTypeResponseDTO);

        RoomTypeResponseDTO response = roomTypeService.updateByRoomTypeId(1, roomTypeRequestDTO);

        assertNotNull(response);
        assertEquals("Deluxe", response.getTypeName());
        verify(roomTypeRepository, times(1)).save(any(RoomType.class));
    }

//    @Test
//    void testUpdateByRoomTypeIdNotFound() {
//        when(roomTypeRepository.findById(anyInt())).thenReturn(Optional.empty());
//
//       RoomTypeException exception = assertThrows(RoomTypeException.class, () -> {
//            roomTypeService.updateByRoomTypeId(1, roomTypeRequestDTO);
//        });
//
//        //assertEquals("Room type with id1not found", exception.getMessage());
//    }

    @Test
    void testFindByRoomTypeId() {
        // Set the roomTypeId using reflection to simulate auto-generation
        setRoomTypeId(roomType, 1);

        when(roomTypeRepository.findById(anyInt())).thenReturn(Optional.of(roomType));
        when(modelMapper.map(any(Optional.class), eq(RoomTypeResponseDTO.class))).thenReturn(roomTypeResponseDTO);

        RoomTypeResponseDTO response = roomTypeService.findByRoomTypeId(1);

        assertNotNull(response);
        assertEquals("Deluxe", response.getTypeName());
    }

//    @Test
//    void testFindByRoomTypeIdNotFound() {
//        when(roomTypeRepository.findById(anyInt())).thenReturn(Optional.empty());
//
//        RoomTypeException exception = assertThrows(RoomTypeException.class, () -> {
//            roomTypeService.findByRoomTypeId(1);
//        });
//
//        assertEquals("Room type with id1not found", exception.getMessage());
//    }

    @Test
    void testDeleteByRoomTypeId() {
        // Set the roomTypeId using reflection to simulate auto-generation
        setRoomTypeId(roomType, 1);

        when(roomTypeRepository.findById(anyInt())).thenReturn(Optional.of(roomType));

        Boolean response = roomTypeService.deleteByRoomTypeId(1);

        assertTrue(response);
        verify(roomTypeRepository, times(1)).deleteById(anyInt());
    }

//    @Test
//    void testDeleteByRoomTypeIdNotFound() {
//        when(roomTypeRepository.findById(anyInt())).thenReturn(Optional.empty());
//
//        RoomTypeException exception = assertThrows(RoomTypeException.class, () -> {
//            roomTypeService.deleteByRoomTypeId(1);
//        });
//
//        assertEquals("Room type with id1not found", exception.getMessage());
//    }

    // Utility method to set roomTypeId via reflection
    private void setRoomTypeId(RoomType roomType, int id) {
        try {
            // Access the private field roomTypeId
            Field field = RoomType.class.getDeclaredField("roomTypeId");
            field.setAccessible(true);
            // Set the roomTypeId field value
            field.set(roomType, id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
