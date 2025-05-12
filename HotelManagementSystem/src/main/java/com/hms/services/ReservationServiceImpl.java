package com.hms.services;
 
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
 
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
import com.hms.dto.ReservationRequestDTO;
import com.hms.dto.ReservationResponseDTO;
import com.hms.execption.DuplicateReservationException;
import com.hms.execption.ResourceNotFoundException;
import com.hms.execption.RoomException;
import com.hms.model.Reservation;
import com.hms.model.Room;
import com.hms.repository.ReservationRepository;
import com.hms.repository.RoomRepository;
 
import jakarta.transaction.Transactional;
 
@Service
public class ReservationServiceImpl implements ReservationServiceIntf {
 
    @Autowired
    private ReservationRepository reservationRepository;
 
    @Autowired
    private RoomRepository roomRepository;
 
    @Autowired
    private ModelMapper modelMapper;
 
    @Override
    @Transactional
    public ReservationResponseDTO addRecord(ReservationRequestDTO requestDTO) throws DuplicateReservationException {
        // Check if a reservation already exists for the given guest email or guest phone
    	 List<Reservation> ll=reservationRepository.findByRoomIdAndReservationsBetweenDates(requestDTO.getCheckInDate(),requestDTO.getCheckOutDate(),requestDTO.getRoomId());
    	if( ll!=null &&  ll.size()>0)
    	{
    		throw new DuplicateReservationException("Room is not avaliable");
    	}

    	boolean emailExists = reservationRepository.existsByGuestEmail(requestDTO.getGuestEmail());
        boolean phoneExists = reservationRepository.existsByGuestPhone(requestDTO.getGuestPhone());
        if (emailExists || phoneExists) {
            throw new DuplicateReservationException("Reservation already exists for the provided email or phone.");
        }
        // Retrieve the Room object for the reservation
        Optional<Room> roomOpt = roomRepository.findById(requestDTO.getRoomId());
        if (!roomOpt.isPresent()) {
            throw new RoomException("Room with ID " + requestDTO.getRoomId() + " does not exist.");
        }
        Room room = roomOpt.get();
        // Ensure the room is still available at the time of saving
        if (!room.getIsAvailable()) {
            throw new RoomException("The Room with ID " + requestDTO.getRoomId() + " is no longer available.");
        }
        Reservation reservation = new Reservation();
        reservation.setGuestName(requestDTO.getGuestName());
        reservation.setGuestEmail(requestDTO.getGuestEmail());
        reservation.setGuestPhone(requestDTO.getGuestPhone());
        reservation.setCheckInDate(requestDTO.getCheckInDate());
        reservation.setCheckOutDate(requestDTO.getCheckOutDate());
        reservation.setRoom(room);
        Reservation savedReservation = reservationRepository.save(reservation);
        // Map the saved reservation to the response DTO
        return modelMapper.map(savedReservation, ReservationResponseDTO.class);
    }
 
    // Get all reservations
    @Override
    public List<ReservationResponseDTO> getAllReservations() throws DuplicateReservationException {
        List<Reservation> reservations = reservationRepository.findAll();
        if (reservations.isEmpty()) {
            throw new DuplicateReservationException("No reservations found.");
        }
        return reservations.stream()
                .map(reservation -> modelMapper.map(reservation, ReservationResponseDTO.class))
                .collect(Collectors.toList());
    }
 
    // Get reservation by ID
    @Override
    public ReservationResponseDTO getReservationById(int reservationId) throws DuplicateReservationException {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new DuplicateReservationException("Reservation with ID " + reservationId + " does not exist."));
        return modelMapper.map(reservation, ReservationResponseDTO.class);
    }
 
    // Get reservations within a date range
    @Override
    public List<ReservationResponseDTO> getReservationsByDateRange(LocalDate startDate, LocalDate endDate) throws DuplicateReservationException {
        List<Reservation> reservations = reservationRepository.findReservationsBetweenDates(startDate, endDate);
        if (reservations.isEmpty()) {
            throw new DuplicateReservationException("No reservations found in the given date range.");
        }
        return reservations.stream()
                .map(reservation -> modelMapper.map(reservation, ReservationResponseDTO.class))
                .collect(Collectors.toList());
    }
 
    @Override
    @Transactional
    public ReservationResponseDTO updateReservation(int reservationId, ReservationRequestDTO requestDTO) {
        Reservation reservation = reservationRepository.findById(reservationId)
            .orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));
        Room room = roomRepository.findById(requestDTO.getRoomId())
        	    .orElseThrow(() -> new RoomException("No room found with roomId: " + requestDTO.getRoomId()));
        reservation.setGuestName(requestDTO.getGuestName());
        reservation.setGuestEmail(requestDTO.getGuestEmail());
        reservation.setRoom(room);
        reservation.setCheckInDate(requestDTO.getCheckInDate());
        reservation.setCheckOutDate(requestDTO.getCheckOutDate());
 
        // Save the entity back to the database with the changes
        reservationRepository.save(reservation);
        return modelMapper.map(reservation, ReservationResponseDTO.class);
    }
 
    // Delete a reservation
    @Override
    public boolean deleteReservation(int reservationId) throws DuplicateReservationException {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new DuplicateReservationException("Reservation with ID " + reservationId + " does not exist."));
        reservationRepository.delete(reservation);
        return true;
    }
}