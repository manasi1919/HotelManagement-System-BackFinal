package com.hms.services;

import java.util.List;

import com.hms.dto.AmenityRequestDTO;
import com.hms.dto.AmenityResponseDTO;

public interface AmenityServiceIntf {
	AmenityResponseDTO addAmenity(AmenityRequestDTO amenityRequestDTO);
    List<AmenityResponseDTO> getAllAmenities();
    AmenityResponseDTO findById(int AmenityId);
    AmenityResponseDTO updateAmenity(int AmenityId, AmenityRequestDTO amenityRequestDTO);
    boolean deleteAmenityById(int AmenityId);
    
    List<AmenityResponseDTO> getHotelById(Integer hotelId);
    List<AmenityResponseDTO> getRoomById(Integer roomId);
}
