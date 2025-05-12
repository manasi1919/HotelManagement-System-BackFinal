package com.hms.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hms.dto.ReviewRequestDTO;
import com.hms.dto.ReviewResponseDTO;
import com.hms.execption.ReviewException;
import com.hms.services.ReviewServiceIntf;

import io.swagger.v3.oas.annotations.Operation;

@CrossOrigin(origins="http://localhost:3000")
@RestController
@RequestMapping("/api/review")
public class ReviewController {
	@Autowired
    private ReviewServiceIntf reviewService;

	@Operation(summary="Get a list of all reviews.")
    @GetMapping("/all")
    public ResponseEntity<?> getAllReviews() {
		List<ReviewResponseDTO> reviews = reviewService.findAll();
        if (reviews.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Map.of("code", "GETALLFAILS", "message", "Review list is empty"));
        }
        return ResponseEntity.ok(Map.of("data", reviews));
    }
	

	@Operation(summary="Upload a new review.")
    @PostMapping("/post")
    public ResponseEntity<?> addReview(@RequestBody ReviewRequestDTO reviewRequestDTO) {
		 try {
	            ReviewResponseDTO response = reviewService.addReview(reviewRequestDTO);
	            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("code", "POSTSUCCESS", "message", "Review added successfully", "data", response));
	        } catch (ReviewException e) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("code", "ADDFAILS", "message", e.getMessage()));
	        }
    }

	@Operation(summary="Get details of a specific review by ID.")
    @GetMapping("/{reviewId}")
    public ResponseEntity<?> getReviewById(@PathVariable (name="reviewId") int reviewId) {
		try {
            ReviewResponseDTO response = reviewService.findByReviewId(reviewId);
            return ResponseEntity.ok(Map.of("data", response));
        } catch (ReviewException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("code", "GETFAILS", "message", e.getMessage()));
        }
    }

	@Operation(summary="Retrieve reviews with a specific rating.")
    @GetMapping("/rating/{rating}")
    public ResponseEntity<List<ReviewResponseDTO>> getReviewsByRating(@PathVariable (name="rating")int rating) {
        return ResponseEntity.ok(reviewService.getReviewsByRating(rating));
    }

	@Operation(summary="Retrieve the most recent reviews.")
    @GetMapping("/recent")
    public ResponseEntity<List<ReviewResponseDTO>> getRecentReviews() {
        return ResponseEntity.ok(reviewService.getRecentReviews());
    }

	@Operation(summary="Update review details.")
    @PutMapping("/update/{reviewId}")
    public ResponseEntity<ReviewResponseDTO> updateReview(@PathVariable (name="reviewId") int reviewId, @RequestBody ReviewRequestDTO reviewRequestDTO) {
        return ResponseEntity.ok(reviewService.updateReview(reviewId, reviewRequestDTO));
    }

	@Operation(summary="Delete an review.")
    @DeleteMapping("/delete/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable (name="reviewId")int reviewId) {
        boolean deleted = reviewService.deleteByReviewId(reviewId);
        if (deleted) {
            return ResponseEntity.ok(Map.of("code", "DELETESUCCESS", "message", "Review deleted successfully"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("code", "DLTFAILS", "message", "Review doesn't exist"));
    }
    
}
