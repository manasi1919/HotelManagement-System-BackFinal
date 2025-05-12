package com.hms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hms.model.Review;

public interface ReviewRepository extends JpaRepository<Review,Integer>{

	
	
	 List<Review> findByRating(int rating);

	    @Query("SELECT r FROM Review r ORDER BY r.reviewDate DESC")
	    List<Review> findRecentReviews();

}
