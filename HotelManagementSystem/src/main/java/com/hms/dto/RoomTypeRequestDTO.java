package com.hms.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomTypeRequestDTO {
	 private Integer roomTypeId;
	 @Column(name = "type_name")
	 @NotNull(message = "Type name cannot be null")
	 @Size(min = 3, max = 50, message = "Type name must be between 3 and 50 characters")
	 private String typeName;
	 @Column(name = "description", columnDefinition = "TEXT")
	 @Size(min = 5, message = "Description should be at least 5 characters")
	 private String description;
	    @Column(name = "price_per_night", precision = 10, scale = 2)
	    @Positive(message = "Price per night must be positive")
	 private Double pricePerNight;
	 @Column(name = "max_occupancy")
     @Min(value = 1, message = "Max occupancy must be at least 1")
	 @Max(value = 10, message = "Max occupancy cannot exceed 10")
	 private int maxOccupancy;
}
