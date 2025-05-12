package com.hms.services;

import java.util.List;

import com.hms.dto.HotelRequestDTO;
import com.hms.dto.HotelResponseDTO;

import jakarta.validation.Valid;

public interface HotelServiceIntf {
	HotelResponseDTO saveHotel(HotelRequestDTO hRequestDTO);
    List<HotelResponseDTO> findAllHotels();
 

    HotelResponseDTO findHotelById(int hotelId);
 
    List<HotelResponseDTO> findHotelsByAmenityId(int amenityId);
 
    HotelResponseDTO updateHotel(int hotelId, @Valid HotelRequestDTO hRequestDTO);
 
    boolean deleteHotelById(int hotelId);

}
