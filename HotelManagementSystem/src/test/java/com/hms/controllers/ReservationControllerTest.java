package com.hms.controllers;
 
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.junit.jupiter.api.Assertions.*;
 
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
 
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
 
import com.hms.controller.ReservationController;
import com.hms.dto.ReservationRequestDTO;
import com.hms.dto.ReservationResponseDTO;
import com.hms.execption.DuplicateReservationException;
import com.hms.services.ReservationServiceIntf;
 
public class ReservationControllerTest {
 
    @Mock
    private ReservationServiceIntf reservationService;
 
    @InjectMocks
    private ReservationController reservationController;
 
    private MockMvc mockMvc;
 
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(reservationController).build();
    }
 
    @Test
    public void testAddReservation() throws Exception {
        ReservationRequestDTO requestDTO = new ReservationRequestDTO("John Doe", "john@example.com", "1234567890", LocalDate.now(), LocalDate.now().plusDays(3), 1);
        ReservationResponseDTO responseDTO = new ReservationResponseDTO(1, "John Doe", "john@example.com", "1234567890", LocalDate.now(), LocalDate.now().plusDays(3), 1);
 
        when(reservationService.addRecord(any(ReservationRequestDTO.class))).thenReturn(responseDTO);
 
        ResponseEntity<?> response = reservationController.addRecord(requestDTO);
 
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Reservation ID: 1"));
        verify(reservationService, times(1)).addRecord(any(ReservationRequestDTO.class));
    }
 
    @Test
    public void testAddDuplicateReservation() throws Exception {
        ReservationRequestDTO requestDTO = new ReservationRequestDTO("John Doe", "john@example.com", "1234567890", LocalDate.now(), LocalDate.now().plusDays(3), 1);
 
        when(reservationService.addRecord(any(ReservationRequestDTO.class))).thenThrow(new DuplicateReservationException("Reservation already exists"));
// 
//        ResponseEntity<?> response = reservationController.addRecord(requestDTO);
// 
//        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//        assertEquals("Reservation already exists", response.getBody());
// 
//        verify(reservationService, times(1)).addRecord(any(ReservationRequestDTO.class));
    }
 
 
    @Test
    public void testGetAllReservations() throws Exception {
        // Mock the service layer
        ReservationResponseDTO reservation1 = new ReservationResponseDTO(1, "John Doe", "john@example.com", "1234567890", LocalDate.now(), LocalDate.now().plusDays(3), 1);
        ReservationResponseDTO reservation2 = new ReservationResponseDTO(2, "Jane Doe", "jane@example.com", "0987654321", LocalDate.now(), LocalDate.now().plusDays(5), 2);
        List<ReservationResponseDTO> reservations = Arrays.asList(reservation1, reservation2);
 
        when(reservationService.getAllReservations()).thenReturn(reservations);
 
        // Perform the GET request
        ResponseEntity<?> response = reservationController.getAllReservations();
 
        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reservations, response.getBody());
 
        // Verify that the service method was called
        verify(reservationService, times(1)).getAllReservations();
    }
 
    @Test
    public void testGetReservationById() throws Exception {
        // Mock the service layer
        ReservationResponseDTO reservation = new ReservationResponseDTO(1, "John Doe", "john@example.com", "1234567890", LocalDate.now(), LocalDate.now().plusDays(3), 1);
 
        when(reservationService.getReservationById(1)).thenReturn(reservation);
 
        // Perform the GET request
        ResponseEntity<?> response = reservationController.getReservationById(1);
 
        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reservation, response.getBody());
 
        // Verify that the service method was called
        verify(reservationService, times(1)).getReservationById(1);
    }
 
    @Test
    public void testUpdateReservation() throws Exception {
        // Mock the service layer
        ReservationRequestDTO requestDTO = new ReservationRequestDTO("John Doe", "john@example.com", "1234567890", LocalDate.now(), LocalDate.now().plusDays(3), 1);
        ReservationResponseDTO updatedReservation = new ReservationResponseDTO(1, "John Doe", "john@example.com", "1234567890", LocalDate.now(), LocalDate.now().plusDays(3), 1);
 
        when(reservationService.updateReservation(1, requestDTO)).thenReturn(updatedReservation);
 
        // Perform the PUT request
        ResponseEntity<?> response = reservationController.updateReservation(1, requestDTO);
 
        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(((Map) response.getBody()).containsKey("message"));
        assertEquals("Reservation updated successfully", ((Map) response.getBody()).get("message"));
 
        // Verify that the service method was called
        verify(reservationService, times(1)).updateReservation(1, requestDTO);
    }
 
    @Test
    public void testDeleteReservation() throws Exception {
        // No need to mock a response DTO, as the service does not return anything
      //  doNothing().when(reservationService).deleteReservation(1);  // mock void method
 
        // Perform the DELETE request
        ResponseEntity<?> response = reservationController.deleteReservation(1);
 
        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(((Map) response.getBody()).containsKey("message"));
        assertEquals("Reservation deleted successfully", ((Map) response.getBody()).get("message"));
 
        // Verify that the service method was called
        verify(reservationService, times(1)).deleteReservation(1);
    }
 
    @Test
    public void testDeleteReservation_NotFound() throws Exception {
        // Mock the service to throw an exception when reservation doesn't exist
        doThrow(new DuplicateReservationException("Reservation doesn't exist")).when(reservationService).deleteReservation(1);
 
        // Perform the DELETE request
        ResponseEntity<?> response = reservationController.deleteReservation(1);
 
        // Verify the response
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(((Map) response.getBody()).containsKey("message"));
        assertEquals("Reservation doesn't exist", ((Map) response.getBody()).get("message"));
 
        // Verify that the service method was called
        verify(reservationService, times(1)).deleteReservation(1);
    }
 
}