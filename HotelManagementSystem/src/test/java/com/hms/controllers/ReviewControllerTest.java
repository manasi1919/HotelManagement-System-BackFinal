package com.hms.controllers;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.hms.dto.ReviewRequestDTO;
import com.hms.dto.ReviewResponseDTO;
import com.hms.execption.ReviewException;
import com.hms.services.ReviewServiceIntf;
import com.hms.controller.ReviewController;

import java.util.List;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;

@SpringBootTest
public class ReviewControllerTest {

    @Mock
    private ReviewServiceIntf reviewService;

    @InjectMocks
    private ReviewController reviewController;

    private MockMvc mockMvc;

    private ReviewRequestDTO reviewRequestDTO;
    private ReviewResponseDTO reviewResponseDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(reviewController).build();

        reviewRequestDTO = new ReviewRequestDTO(1, 1, 5, "Great stay!", LocalDate.now());
        reviewResponseDTO = new ReviewResponseDTO(1,1, 5, "Great stay!", LocalDate.now());
    }

    // Test case for getting all reviews
    @Test
    public void testGetAllReviews() throws Exception {
        List<ReviewResponseDTO> reviews = Arrays.asList(reviewResponseDTO);

        when(reviewService.findAll()).thenReturn(reviews);

        ResponseEntity<?> response = reviewController.getAllReviews();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(((Map) response.getBody()).containsKey("data"));
    }

    // Test case for getting all reviews when no reviews exist
    @Test
    public void testGetAllReviews_Empty() throws Exception {
        List<ReviewResponseDTO> reviews = Arrays.asList();

        when(reviewService.findAll()).thenReturn(reviews);

        ResponseEntity<?> response = reviewController.getAllReviews();
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertTrue(((Map) response.getBody()).containsKey("message"));
    }

    // Test case for adding a review
    @Test
    public void testAddReview() throws ReviewException {
        when(reviewService.addReview(any(ReviewRequestDTO.class)))
            .thenReturn(reviewResponseDTO);

        ResponseEntity<?> response = reviewController.addReview(reviewRequestDTO);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(((Map) response.getBody()).containsKey("message"));
    }

    // Test case for adding a review when an exception is thrown
    @Test
    public void testAddReview_Exception() throws ReviewException {
        // Pass code and message as expected by ReviewException
        when(reviewService.addReview(any(ReviewRequestDTO.class)))
            .thenThrow(new ReviewException("ADDFAILS", "Review failed"));

        ResponseEntity<?> response = reviewController.addReview(reviewRequestDTO);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(((Map) response.getBody()).containsKey("message"));
    }

    // Test case for getting a review by ID
    @Test
    public void testGetReviewById() throws ReviewException {
        when(reviewService.findByReviewId(1)).thenReturn(reviewResponseDTO);

        ResponseEntity<?> response = reviewController.getReviewById(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(((Map) response.getBody()).containsKey("data"));
    }

    // Test case for getting a review by ID when not found
    @Test
    public void testGetReviewById_NotFound() throws ReviewException {
        when(reviewService.findByReviewId(1)).thenThrow(new ReviewException("GETFAILS", "Review not found"));

        ResponseEntity<?> response = reviewController.getReviewById(1);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(((Map) response.getBody()).containsKey("message"));
    }

    // Test case for getting reviews by rating
    @Test
    public void testGetReviewsByRating() throws Exception {
        List<ReviewResponseDTO> reviews = Arrays.asList(reviewResponseDTO);

        when(reviewService.getReviewsByRating(5)).thenReturn(reviews);

        ResponseEntity<List<ReviewResponseDTO>> response = reviewController.getReviewsByRating(5);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().size() > 0);
    }

    // Test case for getting recent reviews
    @Test
    public void testGetRecentReviews() throws Exception {
        List<ReviewResponseDTO> reviews = Arrays.asList(reviewResponseDTO);

        when(reviewService.getRecentReviews()).thenReturn(reviews);

        ResponseEntity<List<ReviewResponseDTO>> response = reviewController.getRecentReviews();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().size() > 0);
    }

    // Test case for updating a review
    @Test
    public void testUpdateReview() throws ReviewException {
        when(reviewService.updateReview(1, reviewRequestDTO)).thenReturn(reviewResponseDTO);

        ResponseEntity<ReviewResponseDTO> response = reviewController.updateReview(1, reviewRequestDTO);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Great stay!", response.getBody().getComment());
    }

    // Test case for deleting a review
    @Test
    public void testDeleteReview() throws ReviewException {
        when(reviewService.deleteByReviewId(1)).thenReturn(true);

        ResponseEntity<?> response = reviewController.deleteReview(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(((Map) response.getBody()).containsKey("message"));
    }

    // Test case for deleting a review when not found
    @Test
    public void testDeleteReview_NotFound() throws ReviewException {
        when(reviewService.deleteByReviewId(1)).thenReturn(false);

        ResponseEntity<?> response = reviewController.deleteReview(1);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(((Map) response.getBody()).containsKey("message"));
    }
}
