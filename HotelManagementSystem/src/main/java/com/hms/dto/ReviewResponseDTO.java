package com.hms.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor

public class ReviewResponseDTO {
	private int reservationId;
	private int reviewId;
	//@ManyToOne
	private int rating;
	private String comment;
	private LocalDate reviewDate;

}
