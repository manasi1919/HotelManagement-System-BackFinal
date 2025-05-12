package com.hms.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hms.model.Room;
@Repository
public interface RoomRepository extends JpaRepository<Room,Integer> {
	
	 List<Room> findByIsAvailableAndRoomType_RoomTypeId(Boolean isAvailable, Integer roomTypeId);
	 List<Room> findByAmenities_AmenityId(Integer amenityId);
	 List<Room> findByAmenities_Hotels_Location(String location);

}
