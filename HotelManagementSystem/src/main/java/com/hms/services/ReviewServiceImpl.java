package com.hms.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
//import org.modelmapper.internal.bytebuddy.dynamic.DynamicType.Builder.RecordComponentDefinition.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hms.dto.ReviewRequestDTO;
import com.hms.dto.ReviewResponseDTO;
import com.hms.execption.ReservationException;
import com.hms.execption.ReviewException;
import com.hms.model.Review;
import com.hms.repository.ReservationRepository;
import com.hms.repository.ReviewRepository;


@Service
public class ReviewServiceImpl implements ReviewServiceIntf{
	 @Autowired
	    private ReviewRepository reviewRepository;
	 
	    @Autowired
	    private ModelMapper modelMapper;
	    @Autowired
	    private ReservationRepository reservationRepository; // Added to fetch reservation

	    @Override
	    public List<ReviewResponseDTO> findAll() {
	        List<Review> reviews = reviewRepository.findAll();
	        if (reviews.isEmpty()) {
	            throw new ReviewException("GETALLFAILS", "Review list is empty");
	        }
	        return reviews.stream()
	                .map(review -> modelMapper.map(review, ReviewResponseDTO.class))
	                .collect(Collectors.toList());
	    }

	  /*  @Override
	    public ReviewResponseDTO addReview(ReviewRequestDTO reviewRequestDTO) {
	        // Fetch the associated reservation before saving the review
	        Reservation reservation = reservationRepository.findById(reviewRequestDTO.getReservationId())
	                .orElseThrow(() -> new ReviewException("GETFAILS", "Reservation not found"));

	        // Map DTO to entity
	        Review review = modelMapper.map(reviewRequestDTO, Review.class);
	        review.setReservationId(reservation.getReservationId()); // Set the relationship

	        // Save and return DTO
	        review = reviewRepository.save(review);
	        return modelMapper.map(review, ReviewResponseDTO.class);
	    }*/
	    
	    @Override
	    public ReviewResponseDTO addReview(ReviewRequestDTO reviewRequestDTO) {
	    	if(!reservationRepository.existsById(reviewRequestDTO.getReservationId())) {
	    		throw new ReservationException("No reservation found");
	    	}
	        if (reviewRepository.existsById(reviewRequestDTO.getReviewId())) {
	            throw new ReviewException("ADDFAILS", "Review already exists");
	        }
	        
	        // Map DTO to entity
	        Review review = modelMapper.map(reviewRequestDTO, Review.class);
	        
	        // Save and return DTO
	        review = reviewRepository.save(review);
	        return modelMapper.map(review, ReviewResponseDTO.class);
	    }

	    @Override
	    public ReviewResponseDTO findByReviewId(int reviewId) {
	    	 Review review = reviewRepository.findById(reviewId)
	    	            .orElseThrow(() -> new ReviewException("GETFAILS", "Review doesn't exist"));

	    	    ReviewResponseDTO responseDTO = modelMapper.map(review, ReviewResponseDTO.class);

	    	    // Manually set reservationId to avoid ModelMapper confusion
	    	    if (review.getReservation() != null) {
	    	        responseDTO.setReservationId(review.getReservation().getReservationId());
	    	    } else {
	    	        throw new ReservationException("No reservation is found");
	    	    }

	    	    return responseDTO;
	    }

	    @Override
	    public List<ReviewResponseDTO> getReviewsByRating(int rating) {
	        List<Review> reviews = reviewRepository.findByRating(rating);
	        if (reviews.isEmpty()) {
	            throw new ReviewException("GETALLFAILS", "Review list is empty");
	        }
	        return reviews.stream()
	                .map(review -> modelMapper.map(review, ReviewResponseDTO.class))
	                .collect(Collectors.toList());
	    }

	    @Override
	    public List<ReviewResponseDTO> getRecentReviews() {
	        List<Review> reviews = reviewRepository.findRecentReviews();
	        if (reviews.isEmpty()) {
	            throw new ReviewException("GETFAILS", "Review doesn't exist");
	        }
	        return reviews.stream()
	                .map(review -> modelMapper.map(review, ReviewResponseDTO.class))
	                .collect(Collectors.toList());
	    }

	    @Override
	    public ReviewResponseDTO updateReview(int reviewId, ReviewRequestDTO reviewRequestDTO) {
	        Optional<Review> reviewOptional = reviewRepository.findById(reviewId);
	        if (!reviewOptional.isPresent()) {
	            throw new ReviewException("UPDTFAILS", "Review doesn't exist");
	        }
	        Review review = reviewOptional.get();
	        review.setRating(reviewRequestDTO.getRating());
	        review.setComment(reviewRequestDTO.getComment());
	        review.setReviewDate(reviewRequestDTO.getReviewDate());

	        review = reviewRepository.save(review);
	        return modelMapper.map(review, ReviewResponseDTO.class);
	    }

	    @Override
	    public boolean deleteByReviewId(int reviewId) {
	        Optional<Review> reviewOptional = reviewRepository.findById(reviewId);
	        if (!reviewOptional.isPresent()) {
	            throw new ReviewException("DLTFAILS", "Review doesn't exist");
	        }
	        reviewRepository.deleteById(reviewId);
	        return true;
	    }
	}

	
	


