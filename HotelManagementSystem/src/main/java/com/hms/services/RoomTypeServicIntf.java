package com.hms.services;

import java.util.List;

import com.hms.dto.RoomTypeRequestDTO;
import com.hms.dto.RoomTypeResponseDTO;


public interface RoomTypeServicIntf {
    List<RoomTypeResponseDTO> findAllRoomType();
    RoomTypeResponseDTO newRoomType(RoomTypeRequestDTO roomTypeRequestDto);
    RoomTypeResponseDTO updateByRoomTypeId(Integer roomTypeId, RoomTypeRequestDTO roomTypeRequestDto);
    RoomTypeResponseDTO findByRoomTypeId(Integer roomTypeId);
    Boolean deleteByRoomTypeId(Integer roomTypeId);
}

