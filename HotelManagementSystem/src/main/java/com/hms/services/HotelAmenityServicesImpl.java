package com.hms.services;


import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hms.dto.HotelAmenityRequestDTO;
import com.hms.dto.HotelAmenityResponseDTO;
import com.hms.model.Amenity;
import com.hms.model.Hotel;
import com.hms.repository.AmenityRepository;
import com.hms.repository.HotelRepository;

@Service
public class HotelAmenityServicesImpl implements HotelAmenityServicesIntf {
 
    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AmenityRepository amenityRepository;
 
    public HotelAmenityResponseDTO associateAmenitiesToHotel(HotelAmenityRequestDTO hotelAmenityRequestDTO) {
        Optional<Hotel> hotelOpt = hotelRepository.findById(hotelAmenityRequestDTO.getHotelId());
        if (hotelOpt.isPresent()) {
            Hotel hotel = hotelOpt.get();
            // Log the fetched hotel
            System.out.println("Hotel found: " + hotel.getHotelId());
 
            List<Amenity> amenities = amenityRepository.findAllById(hotelAmenityRequestDTO.getAmenityId());
            // Log the fetched amenities
            System.out.println("Amenities fetched: " + amenities.size());
 
            // Add the new amenities to the hotel's existing amenities set
            hotel.getAmenities().addAll(amenities);
 
            // Log before saving
            System.out.println("Saving hotel with updated amenities...");
 
            // Save the updated hotel with the new amenities
            return modelMapper.map(hotelRepository.save(hotel),HotelAmenityResponseDTO.class);
        } else {
            throw new RuntimeException("Hotel not found with ID " + hotelAmenityRequestDTO.getHotelId());
        }
    }
 
}