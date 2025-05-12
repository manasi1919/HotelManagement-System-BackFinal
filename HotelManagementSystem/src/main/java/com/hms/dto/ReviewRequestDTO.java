package com.hms.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRequestDTO {
	private int reviewId;
	//@ManyToOne
	
	@Min(value = 1, message = "Reservation ID must be greater than 0")
	private int reservationId; 

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private int rating;

    @NotBlank(message = "Comment cannot be blank")
    private String comment;

    @NotBlank(message = "Review date cannot be null")
    private LocalDate reviewDate;

}
