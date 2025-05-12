package com.hms.services;

import java.time.LocalDate;
import java.util.List;

import com.hms.dto.ReservationRequestDTO;
import com.hms.dto.ReservationResponseDTO;
import com.hms.execption.DuplicateReservationException;

public interface ReservationServiceIntf {
	 
	// Create a new reservation
    ReservationResponseDTO addRecord(ReservationRequestDTO requestDTO) throws DuplicateReservationException;
    // Get all reservations
    List<ReservationResponseDTO> getAllReservations() throws DuplicateReservationException;
    // Get reservation by ID
    ReservationResponseDTO getReservationById(int reservationId) throws DuplicateReservationException;
    // Get reservations within a date range
    List<ReservationResponseDTO> getReservationsByDateRange(LocalDate startDate, LocalDate endDate) throws DuplicateReservationException;
    // Update reservation details
    ReservationResponseDTO updateReservation(int reservationId, ReservationRequestDTO requestDTO) throws DuplicateReservationException;
    // Delete reservation
    boolean deleteReservation(int reservationId) throws DuplicateReservationException;
}