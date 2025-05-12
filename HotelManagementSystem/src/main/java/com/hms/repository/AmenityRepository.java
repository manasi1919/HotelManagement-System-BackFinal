package com.hms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hms.model.Amenity;
@Repository
public interface AmenityRepository extends JpaRepository<Amenity, Integer>{
	@Query(value = "SELECT a.* FROM amenity a JOIN hotelamenity ha ON a.amenity_id = ha.amenity_id WHERE ha.hotel_id = :hotel_id", nativeQuery = true)
	  List<Amenity> findAmenitiesByHotelId(@Param("hotelId") int hotelId);
	@Query(value = "SELECT a.* FROM amenity a JOIN roomamenity ra ON a.amenity_id = ra.amenity_id WHERE ra.room_id = :room_id", nativeQuery = true)
	  List<Amenity> findAmenitiesByRoomId(@Param("roomId") int roomId);
}
