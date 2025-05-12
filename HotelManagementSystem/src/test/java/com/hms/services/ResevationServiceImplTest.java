package com.hms.services;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import com.hms.dto.ReservationRequestDTO;
import com.hms.dto.ReservationResponseDTO;
import com.hms.execption.DuplicateReservationException;
import com.hms.execption.RoomException;
import com.hms.model.Reservation;
import com.hms.model.Room;
import com.hms.repository.ReservationRepository;
import com.hms.repository.RoomRepository;
import com.hms.services.ReservationServiceImpl;
import org.modelmapper.ModelMapper;

@SpringBootTest
public class ResevationServiceImplTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    private ReservationRequestDTO requestDTO;
    private ReservationResponseDTO responseDTO;
    private Room room;
    private Reservation reservation;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        room = new Room();
        room.setRoomId(1);
        room.setIsAvailable(true);

        reservation = new Reservation();
        reservation.setReservationId(1);
        reservation.setGuestName("John Doe");
        reservation.setGuestEmail("john@example.com");
        reservation.setGuestPhone("1234567890");
        reservation.setCheckInDate(LocalDate.now());
        reservation.setCheckOutDate(LocalDate.now().plusDays(3));
        reservation.setRoom(room);

        requestDTO = new ReservationRequestDTO("John Doe", "john@example.com", "1234567890", LocalDate.now(), LocalDate.now().plusDays(3), 1);
        responseDTO = new ReservationResponseDTO(1, "John Doe", "john@example.com", "1234567890", LocalDate.now(), LocalDate.now().plusDays(3), 1);
    }

    // Test case for adding a reservation
    @Test
    public void testAddRecord() throws DuplicateReservationException {
        when(reservationRepository.existsByGuestEmail(requestDTO.getGuestEmail())).thenReturn(false);
        when(reservationRepository.existsByGuestPhone(requestDTO.getGuestPhone())).thenReturn(false);
        when(roomRepository.findById(requestDTO.getRoomId())).thenReturn(Optional.of(room));
        when(modelMapper.map(reservation, ReservationResponseDTO.class)).thenReturn(responseDTO);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        ReservationResponseDTO result = reservationService.addRecord(requestDTO);

        assertNotNull(result);
        assertEquals("John Doe", result.getGuestName());
        assertEquals("john@example.com", result.getGuestEmail());
        assertEquals("1234567890", result.getGuestPhone());
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    // Test case for adding a reservation with an existing email
    @Test
    public void testAddRecord_DuplicateEmail() {
        when(reservationRepository.existsByGuestEmail(requestDTO.getGuestEmail())).thenReturn(true);

        assertThrows(DuplicateReservationException.class, () -> {
            reservationService.addRecord(requestDTO);
        });
    }

    // Test case for adding a reservation with an existing phone number
    @Test
    public void testAddRecord_DuplicatePhone() {
        when(reservationRepository.existsByGuestPhone(requestDTO.getGuestPhone())).thenReturn(true);

        assertThrows(DuplicateReservationException.class, () -> {
            reservationService.addRecord(requestDTO);
        });
    }

    // Test case for getting all reservations
    @Test
    public void testGetAllReservations() throws DuplicateReservationException {
        when(reservationRepository.findAll()).thenReturn(Arrays.asList(reservation));
        when(modelMapper.map(reservation, ReservationResponseDTO.class)).thenReturn(responseDTO);

        List<ReservationResponseDTO> result = reservationService.getAllReservations();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getGuestName());
    }

    // Test case for getting a reservation by ID
    @Test
    public void testGetReservationById() throws DuplicateReservationException {
        when(reservationRepository.findById(1)).thenReturn(Optional.of(reservation));
        when(modelMapper.map(reservation, ReservationResponseDTO.class)).thenReturn(responseDTO);

        ReservationResponseDTO result = reservationService.getReservationById(1);

        assertNotNull(result);
        assertEquals("John Doe", result.getGuestName());
        assertEquals("john@example.com", result.getGuestEmail());
    }

    // Test case for getting a reservation by ID when not found
    @Test
    public void testGetReservationById_NotFound() {
        when(reservationRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(DuplicateReservationException.class, () -> {
            reservationService.getReservationById(1);
        });
    }

    // Test case for getting reservations by date range
    @Test
    public void testGetReservationsByDateRange() throws DuplicateReservationException {
        when(reservationRepository.findReservationsBetweenDates(LocalDate.now(), LocalDate.now().plusDays(5)))
                .thenReturn(Arrays.asList(reservation));
        when(modelMapper.map(reservation, ReservationResponseDTO.class)).thenReturn(responseDTO);

        List<ReservationResponseDTO> result = reservationService.getReservationsByDateRange(LocalDate.now(), LocalDate.now().plusDays(5));

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getGuestName());
    }

    // Test case for updating a reservation
    @Test
    public void testUpdateReservation() throws DuplicateReservationException {
        ReservationRequestDTO updateRequestDTO = new ReservationRequestDTO("Jane Doe", "jane@example.com", "0987654321", LocalDate.now(), LocalDate.now().plusDays(2), 1);
        when(reservationRepository.findById(1)).thenReturn(Optional.of(reservation));
        when(roomRepository.findById(updateRequestDTO.getRoomId())).thenReturn(Optional.of(room));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        when(modelMapper.map(reservation, ReservationResponseDTO.class)).thenReturn(responseDTO);

        ReservationResponseDTO result = reservationService.updateReservation(1, updateRequestDTO);

        assertNotNull(result);
//        assertEquals("Jane Doe", result.getGuestName());
//        assertEquals("jane@example.com", result.getGuestEmail());
    }

    // Test case for deleting a reservation
    @Test
    public void testDeleteReservation() throws DuplicateReservationException {
        when(reservationRepository.findById(1)).thenReturn(Optional.of(reservation));

        boolean result = reservationService.deleteReservation(1);

        assertTrue(result);
        verify(reservationRepository, times(1)).delete(reservation);
    }

    // Test case for deleting a reservation when not found
    @Test
    public void testDeleteReservation_NotFound() {
        when(reservationRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(DuplicateReservationException.class, () -> {
            reservationService.deleteReservation(1);
        });
    }
}
