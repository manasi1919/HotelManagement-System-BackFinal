package com.hms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hms.model.Hotel;
@Repository
public interface HotelRepository extends JpaRepository<Hotel,Integer>{
	List<Hotel> findByAmenities_AmenityId(Integer amenityId);

}
