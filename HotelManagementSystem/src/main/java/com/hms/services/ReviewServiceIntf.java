package com.hms.services;

import java.util.List;

import com.hms.dto.ReviewRequestDTO;
import com.hms.dto.ReviewResponseDTO;

public interface ReviewServiceIntf {
	  List<ReviewResponseDTO> findAll();
	    
	    ReviewResponseDTO addReview(ReviewRequestDTO reviewRequestDTO);
	    
	    ReviewResponseDTO findByReviewId(int reviewId);
	    
	    List<ReviewResponseDTO> getReviewsByRating(int rating);
	   
	    List<ReviewResponseDTO> getRecentReviews();
	    
	    
	    ReviewResponseDTO updateReview(int reviewId, ReviewRequestDTO reviewRequestDTO);
	    
	    boolean deleteByReviewId(int reviewId);

}
