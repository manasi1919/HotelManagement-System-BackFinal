package com.hms.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomRequestDTO {
	private Integer roomId;
	@Column(nullable = false)
	@NotNull(message = "Room number cannot be null")
    @Min(value = 1, message = "Room number must be a positive integer")
	private Integer roomNumber;
	@Column(nullable = false)
	private Boolean isAvailable;
	@Column(nullable = false)
	private Integer roomTypeId;

}
