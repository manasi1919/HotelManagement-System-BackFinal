package com.hms.services;
import com.hms.dto.HotelAmenityRequestDTO;
import com.hms.dto.HotelAmenityResponseDTO;
public interface HotelAmenityServicesIntf {
	HotelAmenityResponseDTO associateAmenitiesToHotel(HotelAmenityRequestDTO roomRequestDTO);
}
