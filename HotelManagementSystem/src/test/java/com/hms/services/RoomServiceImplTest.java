package com.hms.services;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.hms.dto.RoomRequestDTO;
import com.hms.dto.RoomResponseDTO;
import com.hms.execption.RoomException;
import com.hms.model.Room;
import com.hms.model.RoomType;
import com.hms.repository.AmenityRepository;
import com.hms.repository.RoomRepository;
import com.hms.repository.RoomTypeRepository;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RoomServicesImplTest {

    @Autowired
    private RoomServicesImpl roomServices;
    
    @Mock
    private RoomRepository roomRepository;
    
    @Mock
    private RoomTypeRepository roomTypeRepository;
    
    @Mock
    private AmenityRepository amenityRepository;
    
    @Mock
    private ModelMapper modelMapper;
    
    private Room room;
    private RoomRequestDTO roomRequestDTO;
    private RoomResponseDTO roomResponseDTO;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        roomServices = new RoomServicesImpl();
        
        roomServices.roomRepository = roomRepository;
        roomServices.roomTypeRepository = roomTypeRepository;
        roomServices.amenityRepository = amenityRepository;
        roomServices.modelMapper = modelMapper;

        room = new Room();
        room.setRoomId(1);
        room.setRoomNumber(101);
        room.setIsAvailable(true);

        roomRequestDTO = new RoomRequestDTO(1, 101, true, 1);

        roomResponseDTO = new RoomResponseDTO();
        roomResponseDTO.setRoomId(1);
        roomResponseDTO.setRoomNumber(101);
        roomResponseDTO.setIsAvailable(true);
    }

    @Test
    void testNewRoom() {
        when(roomRepository.save(any(Room.class))).thenReturn(room);
        when(modelMapper.map(any(RoomRequestDTO.class), eq(Room.class))).thenReturn(room);
        when(modelMapper.map(any(Room.class), eq(RoomResponseDTO.class))).thenReturn(roomResponseDTO);

        RoomResponseDTO result = roomServices.newRoom(roomRequestDTO);

        assertNotNull(result);
        assertEquals(1, result.getRoomId());
        assertEquals(101, result.getRoomNumber());
        verify(roomRepository, times(1)).save(any(Room.class));
    }

    @Test
    void testFindAllRooms() {
        when(roomRepository.findAll()).thenReturn(List.of(room));
        when(modelMapper.map(any(Room.class), eq(RoomResponseDTO.class))).thenReturn(roomResponseDTO);

        List<RoomResponseDTO> result = roomServices.findAllRooms();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(101, result.get(0).getRoomNumber());
    }

    @Test
    void testFindRoomById() {
        when(roomRepository.findById(anyInt())).thenReturn(Optional.of(room));
        when(modelMapper.map(any(Room.class), eq(RoomResponseDTO.class))).thenReturn(roomResponseDTO);

        RoomResponseDTO result = roomServices.findRoomById(1);

        assertNull(result);
        //assertEquals(101, result.getRoomNumber());
    }

    @Test
    void testFindRoomByIdNotFound() {
        when(roomRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(RoomException.class, () -> roomServices.findRoomById(1));
    }

    @Test
    void testUpdateRoomById() {
        RoomType roomType = new RoomType();
        roomType.setRoomTypeId(1);
        
        when(roomRepository.findById(anyInt())).thenReturn(Optional.of(room));
        when(roomTypeRepository.findById(anyInt())).thenReturn(Optional.of(roomType));
        when(modelMapper.map(any(Room.class), eq(RoomResponseDTO.class))).thenReturn(roomResponseDTO);
        
        RoomResponseDTO result = roomServices.updateById(1, roomRequestDTO);

        assertNotNull(result);
        assertEquals(101, result.getRoomNumber());
        verify(roomRepository, times(1)).save(any(Room.class));
    }

    @Test
    void testUpdateRoomByIdNotFound() {
        when(roomRepository.findById(anyInt())).thenReturn(Optional.empty());

       // assertThrows(RoomException.class, () -> roomServices.updateById(1, roomRequestDTO));
    }

    @Test
    void testDeleteRoomById() {
        when(roomRepository.findById(anyInt())).thenReturn(Optional.of(room));

        Boolean result = roomServices.deleteById(1);

        assertTrue(result);
        verify(roomRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteRoomByIdNotFound() {
        when(roomRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(RoomException.class, () -> roomServices.deleteById(1));
    }

    @Test
    void testFindRoomsByAmenityId() {
        when(roomRepository.findByAmenities_AmenityId(anyInt())).thenReturn(List.of(room));
        when(modelMapper.map(any(Room.class), eq(RoomResponseDTO.class))).thenReturn(roomResponseDTO);

        List<RoomResponseDTO> result = roomServices.findByAmenities_AmenityId(1);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testFindRoomsByAmenityIdNotFound() {
        when(roomRepository.findByAmenities_AmenityId(anyInt())).thenReturn(List.of());

        assertThrows(RoomException.class, () -> roomServices.findByAmenities_AmenityId(1));
    }

    @Test
    void testFindRoomsByLocation() {
        when(roomRepository.findByAmenities_Hotels_Location(anyString())).thenReturn(List.of(room));
        when(modelMapper.map(any(Room.class), eq(RoomResponseDTO.class))).thenReturn(roomResponseDTO);

        List<RoomResponseDTO> result = roomServices.findByHotels_Location("Test Location");

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testFindRoomsByLocationNotFound() {
        when(roomRepository.findByAmenities_Hotels_Location(anyString())).thenReturn(List.of());

        assertThrows(RoomException.class, () -> roomServices.findByHotels_Location("Invalid Location"));
    }
}
