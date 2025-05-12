package com.hms.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hms.dto.HotelRequestDTO;
import com.hms.dto.HotelResponseDTO;
import com.hms.execption.HotelException;
import com.hms.model.Hotel;
import com.hms.repository.HotelRepository;

import jakarta.validation.Valid;

@Service
public class HotelServiceImpl implements HotelServiceIntf {
	@Autowired
	private HotelRepository hotelRepository;
	@Autowired
	private ModelMapper modelMapper;
 
	@Override
	public HotelResponseDTO saveHotel(HotelRequestDTO hRequestDTO) {
		// TODO Auto-generated method stub
		Hotel h=modelMapper.map(hRequestDTO,Hotel.class);
		h=hotelRepository.save(h);
		return modelMapper.map(h,HotelResponseDTO.class);
	}
 
	@Override
	public List<HotelResponseDTO> findAllHotels() {
		// TODO Auto-generated method stub
		return hotelRepository.findAll().
				stream().map(hotel->modelMapper.map(hotel, HotelResponseDTO.class))
				.collect(Collectors.toList());
	}
 
	@Override
	public HotelResponseDTO findHotelById(int hotelId) {
		// TODO Auto-generated method stub
		Optional<Hotel> hotelOptional=hotelRepository.findById(hotelId);
		if(hotelOptional.isPresent()) {
			return modelMapper.map(hotelOptional.get(), HotelResponseDTO.class);
		}
		return null;
	}
 
 
	@Override
	public List<HotelResponseDTO> findHotelsByAmenityId(int amenityId) {
		// TODO Auto-generated method stub
		List<Hotel> hotelOptional=hotelRepository.findByAmenities_AmenityId(amenityId);
		if(hotelOptional.size()>0) {
			return hotelRepository.findByAmenities_AmenityId(amenityId)
					.stream()
					.map(hotels->modelMapper.map(hotels, HotelResponseDTO.class))
					.collect(Collectors.toList());
		}
		else {
			throw new HotelException("hotels with amenits are not avaiable."+amenityId);
		}
	}
 
	@Override
	public HotelResponseDTO updateHotel(int hotelId, @Valid HotelRequestDTO hotelRequestDTO) {
		// TODO Auto-generated method stub
		 Optional<Hotel> hotelOptional = hotelRepository.findById(hotelId);
		    if (hotelOptional.isPresent()) {
		        Hotel existingHotel = hotelOptional.get();
		        modelMapper.map(hotelRequestDTO, existingHotel);
		        Hotel updatedHotel = hotelRepository.save(existingHotel);
		        return modelMapper.map(updatedHotel, HotelResponseDTO.class);
		    } else {
		        // Handle the case where the patient is not found
		        return null;
		    }
	}
 
	@Override
	public boolean deleteHotelById(int hotelId) {
		// TODO Auto-generated method stub
		Optional<Hotel> hotelOptional= hotelRepository.findById(hotelId);
		if(hotelOptional.isPresent()) {
			hotelRepository.deleteById(hotelId);
			return true;
		}
		return false;
	}
}
