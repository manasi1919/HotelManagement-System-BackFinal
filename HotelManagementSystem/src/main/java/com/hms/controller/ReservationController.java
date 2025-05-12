package com.hms.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hms.dto.ReservationRequestDTO;
import com.hms.dto.ReservationResponseDTO;
import com.hms.execption.DuplicateReservationException;
import com.hms.services.ReservationServiceIntf;

import jakarta.validation.Valid;
@CrossOrigin(origins="http://localhost:3000")
@RestController
@RequestMapping("/api/reservation")
public class ReservationController {

    @Autowired
    private ReservationServiceIntf reservationService;

 // 1️ Create a new reservation
    @PostMapping("/post")public ResponseEntity<String> addRecord(@Valid @RequestBody ReservationRequestDTO requestDTO) throws DuplicateReservationException {
        ReservationResponseDTO responseDTO = reservationService.addRecord(requestDTO);
        
        if (responseDTO != null) {
            String responseText = String.format(
                "Reservation ID: %d\nGuest Name: %s\nGuest Email: %s\nGuest Phone: %s\nCheck-In Date: %s\nCheck-Out Date: %s\nRoom ID: %d",
                responseDTO.getReservationId(),
                responseDTO.getGuestName(),
                responseDTO.getGuestEmail(),
                responseDTO.getGuestPhone(),
                responseDTO.getCheckInDate(),
                responseDTO.getCheckOutDate(),
                responseDTO.getRoomId()
            );
            return new ResponseEntity<>(responseText, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Reservation already exists", HttpStatus.BAD_REQUEST);
        }
    }

    // 2️ Get all reservations
    @GetMapping("/all")
    public ResponseEntity<?> getAllReservations() throws DuplicateReservationException {
        List<ReservationResponseDTO> reservations = reservationService.getAllReservations();
        if (reservations.isEmpty()) {
            return new ResponseEntity<>(
                Map.of("code", "GETALLFAILS", "message", "Reservation list is empty"),
                HttpStatus.NO_CONTENT
            );
        }
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    // 3️ Get a reservation by ID
    @GetMapping("/{reservationId}")
    public ResponseEntity<?> getReservationById(@PathVariable("reservationId") int reservationId) throws DuplicateReservationException {
        ReservationResponseDTO reservation = reservationService.getReservationById(reservationId);
        if (reservation != null) {
            return new ResponseEntity<>(reservation, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                Map.of("code", "GETFAILS", "message", "Reservation doesn't exist"),
                HttpStatus.NOT_FOUND
            );
        }
    }

    // 4️ Get reservations by date range
    @GetMapping("/date-range")
    public ResponseEntity<?> getReservationsByDateRange(
            @RequestParam(name = "startDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,

            @RequestParam(name = "endDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) throws DuplicateReservationException {

        if (startDate == null || endDate == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Both startDate and endDate are required in yyyy-MM-dd format.");
        }

        List<ReservationResponseDTO> reservations = reservationService.getReservationsByDateRange(startDate, endDate);
        if (reservations.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    // 5️ Update reservation
    @PutMapping("/update/{reservationId}")
    public ResponseEntity<?> updateReservation(
            @PathVariable("reservationId") int reservationId,
            @Valid @RequestBody ReservationRequestDTO requestDTO) throws DuplicateReservationException {
        ReservationResponseDTO updatedReservation = reservationService.updateReservation(reservationId, requestDTO);
        if (updatedReservation != null) {
            return new ResponseEntity<>(
                Map.of("code", "UPDATESUCCESS", "message", "Reservation updated successfully", "data", updatedReservation),
                HttpStatus.OK
            );
        } else {
            return new ResponseEntity<>(
                Map.of("code", "UPDTFAILS", "message", "Reservation doesn't exist"),
                HttpStatus.NOT_FOUND
            );
        }
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<?> deleteReservation(@PathVariable(name = "reservationId") int reservationId) {
        try {
            reservationService.deleteReservation(reservationId);
            return new ResponseEntity<>(
                Map.of("code", "DELETESUCCESS", "message", "Reservation deleted successfully"),
                HttpStatus.OK
            );
        } catch (DuplicateReservationException ex) {
            return new ResponseEntity<>(
                Map.of("code", "DLTFAILS", "message", "Reservation doesn't exist"),
                HttpStatus.NOT_FOUND
            );
        }
    }
}
