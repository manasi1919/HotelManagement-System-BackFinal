package com.hms.repository;
 
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
 
import com.hms.model.Reservation;
 
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
	 @Query("SELECT r FROM Reservation r WHERE r.checkInDate >= :startDate AND r.checkOutDate <= :endDate")
		List<Reservation> findReservationsBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
	 // List<Reservation> findByCheckInDateGreaterThanEqualAndCheckOutDateLessThanEqual(LocalDate startDate, LocalDate endDate);
	 @Query("SELECT r FROM Reservation r WHERE r.checkInDate >= :startDate AND r.checkOutDate <= :endDate AND r.room.roomId = :roomId")
	 List<Reservation> findByRoomIdAndReservationsBetweenDates(
	     @Param("startDate") LocalDate startDate, 
	     @Param("endDate") LocalDate endDate,
	     @Param("roomId") int roomId
	 );
 
    boolean existsByGuestEmail(String guestEmail);  
    boolean existsByGuestPhone(String guestPhone); 
}