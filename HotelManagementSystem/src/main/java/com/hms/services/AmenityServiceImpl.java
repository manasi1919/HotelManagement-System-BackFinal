package com.hms.services;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hms.dto.AmenityRequestDTO;
import com.hms.dto.AmenityResponseDTO;
import com.hms.execption.AmenityNotFoundException;
import com.hms.execption.ResourceNotFoundException;
import com.hms.model.Amenity;
import com.hms.repository.AmenityRepository;
import jakarta.transaction.Transactional;

@Service
public class AmenityServiceImpl implements AmenityServiceIntf {

    @Autowired
    private AmenityRepository amenityRepository;

    @Autowired
    private ModelMapper modelMapper;

    public AmenityResponseDTO addAmenity(AmenityRequestDTO amenityRequestDTO) {
        Amenity amenity = modelMapper.map(amenityRequestDTO, Amenity.class);
        Amenity savedAmenity = amenityRepository.save(amenity);
        return modelMapper.map(savedAmenity, AmenityResponseDTO.class);
    }

    public AmenityResponseDTO findById(int amenityId) {
        Amenity amenity = amenityRepository.findById(amenityId)
            .orElseThrow(() -> new ResourceNotFoundException("Amenity not found with id " + amenityId));
        return modelMapper.map(amenity, AmenityResponseDTO.class);
    }

    @Transactional
    public boolean deleteAmenityById(int amenityId) {
        if (!amenityRepository.existsById(amenityId)) {
            throw new ResourceNotFoundException("Amenity not found with id " + amenityId);
        }
        amenityRepository.deleteById(amenityId);
        return !amenityRepository.existsById(amenityId);
    }

    public List<AmenityResponseDTO> getAllAmenities() {
        return amenityRepository.findAll()
                .stream()
                .map(amenity -> modelMapper.map(amenity, AmenityResponseDTO.class))
                .collect(Collectors.toList());
    }

    public AmenityResponseDTO updateAmenity(int amenityId, AmenityRequestDTO amenityRequestDTO) {
        Amenity existingAmenity = amenityRepository.findById(amenityId)
            .orElseThrow(() -> new ResourceNotFoundException("Amenity not found with id " + amenityId));

        if (amenityRequestDTO.getName() != null) {
            existingAmenity.setName(amenityRequestDTO.getName());
        }
        if (amenityRequestDTO.getDescription() != null) {
            existingAmenity.setDescription(amenityRequestDTO.getDescription());
        }

        Amenity updatedAmenity = amenityRepository.save(existingAmenity);
        return modelMapper.map(updatedAmenity, AmenityResponseDTO.class);
    }

    public List<AmenityResponseDTO> getRoomById(Integer roomId) {
        if (roomId == null) {
            throw new AmenityNotFoundException("Room ID cannot be null");
        }
        List<Amenity> amenities = amenityRepository.findAmenitiesByRoomId(roomId);
        if (amenities.isEmpty()) {
            throw new AmenityNotFoundException("No amenities found for the given room ID");
        }
        return amenities.stream()
        		.map(amenity->modelMapper.map(amenity,AmenityResponseDTO.class))
        		.collect(Collectors.toList());
    }

    public List<AmenityResponseDTO> getHotelById(Integer hotelId) {
        if (hotelId == null) {
            throw new AmenityNotFoundException("Hotel ID cannot be null");
        }
        List<Amenity> amenities = amenityRepository.findAmenitiesByHotelId(hotelId);
        if (amenities.isEmpty()) {
            throw new AmenityNotFoundException("No amenities found for the given hotel ID");
        }
        return amenities.stream()
        		.map(amenity->modelMapper.map(amenity,AmenityResponseDTO.class))
        		.collect(Collectors.toList());
    }






}
