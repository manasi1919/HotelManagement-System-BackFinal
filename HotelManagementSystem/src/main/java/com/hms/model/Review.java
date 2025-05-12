package com.hms.model;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Review {
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private int reviewId;
	@Column(nullable = false)
    private int rating;
	@Column(nullable = true)
    private String comment;
	@Column(nullable = false)
    private LocalDate reviewDate;
    
    @ManyToOne
    @JoinColumn(name = "reservation_id")  // Ensure this matches the physical column name in DB
    private Reservation reservation;
}

