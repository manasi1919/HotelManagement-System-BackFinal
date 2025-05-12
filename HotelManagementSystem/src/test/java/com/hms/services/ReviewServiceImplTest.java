package com.hms.services;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.modelmapper.ModelMapper;
import com.hms.dto.ReviewRequestDTO;
import com.hms.dto.ReviewResponseDTO;
import com.hms.execption.ReviewException;
import com.hms.model.Review;
import com.hms.model.Reservation;
import com.hms.repository.ReviewRepository;
import com.hms.repository.ReservationRepository;

import java.util.Arrays;
import java.util.Optional;
import java.util.List;
import java.time.LocalDate;

@SpringBootTest
public class ReviewServiceImplTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    private ReviewRequestDTO reviewRequestDTO;
    private ReviewResponseDTO reviewResponseDTO;
    private Review review;
    private Reservation reservation;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        reservation = new Reservation();
        reservation.setReservationId(1);
        reservation.setGuestName("John Doe");
        reservation.setGuestEmail("john.doe@example.com");
        reservation.setGuestPhone("1234567890");
        reservation.setCheckInDate(LocalDate.of(2025, 3, 13));
        reservation.setCheckOutDate(LocalDate.of(2025, 3, 15));

        reviewRequestDTO = new ReviewRequestDTO(1, 1, 5, "Great stay!", LocalDate.now());
        reviewResponseDTO = new ReviewResponseDTO(1, 1, 5, "Great stay!", LocalDate.now());

        review = new Review(1, 5, "Great stay!", LocalDate.now(), reservation);
    }

    @Test
    public void testFindAll() {
        when(reviewRepository.findAll()).thenReturn(Arrays.asList(review));
        when(modelMapper.map(any(Review.class), eq(ReviewResponseDTO.class))).thenReturn(reviewResponseDTO);

        List<ReviewResponseDTO> reviews = reviewService.findAll();

        assertNotNull(reviews);
        assertEquals(1, reviews.size());
        assertEquals("Great stay!", reviews.get(0).getComment());
    }

    @Test
    public void testFindAll_Empty() {
        when(reviewRepository.findAll()).thenReturn(Arrays.asList());

        ReviewException exception = assertThrows(ReviewException.class, () -> reviewService.findAll());

        assertEquals("GETALLFAILS", exception.getCode());
        assertEquals("Review list is empty", exception.getMessage());
    }

    @Test
    public void testAddReview() {
        when(reviewRepository.existsById(anyInt())).thenReturn(false);
        when(reservationRepository.findById(anyInt())).thenReturn(Optional.of(reservation));
        when(modelMapper.map(any(ReviewRequestDTO.class), eq(Review.class))).thenReturn(review);
        when(reviewRepository.save(any(Review.class))).thenReturn(review);
        when(modelMapper.map(any(Review.class), eq(ReviewResponseDTO.class))).thenReturn(reviewResponseDTO);
//
//        ReviewResponseDTO response = reviewService.addReview(reviewRequestDTO);
//
//        assertNotNull(response);
//        assertEquals("Great stay!", response.getComment());
//
//        verify(reviewRepository, times(1)).existsById(anyInt());
//        verify(reservationRepository, times(1)).findById(anyInt());
//        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    public void testAddReview_AlreadyExists() {
        when(reviewRepository.existsById(anyInt())).thenReturn(true);
//
//        ReviewException exception = assertThrows(ReviewException.class, () -> reviewService.addReview(reviewRequestDTO));
//
//        assertEquals("ADDFAILS", exception.getCode());
//        assertEquals("Review already exists", exception.getMessage());
    }

    @Test
    public void testFindByReviewId() {
        when(reviewRepository.findById(1)).thenReturn(Optional.of(review));
        when(modelMapper.map(any(Review.class), eq(ReviewResponseDTO.class))).thenReturn(reviewResponseDTO);

        ReviewResponseDTO response = reviewService.findByReviewId(1);

        assertNotNull(response);
        assertEquals("Great stay!", response.getComment());
    }

    @Test
    public void testFindByReviewId_NotFound() {
        when(reviewRepository.findById(1)).thenReturn(Optional.empty());

        ReviewException exception = assertThrows(ReviewException.class, () -> reviewService.findByReviewId(1));

        assertEquals("GETFAILS", exception.getCode());
        assertEquals("Review doesn't exist", exception.getMessage());
    }

    @Test
    public void testGetReviewsByRating() {
        when(reviewRepository.findByRating(5)).thenReturn(Arrays.asList(review));
        when(modelMapper.map(any(Review.class), eq(ReviewResponseDTO.class))).thenReturn(reviewResponseDTO);

        List<ReviewResponseDTO> reviews = reviewService.getReviewsByRating(5);

        assertNotNull(reviews);
        assertEquals(1, reviews.size());
        assertEquals(5, reviews.get(0).getRating());
    }

    @Test
    public void testGetRecentReviews() {
        when(reviewRepository.findRecentReviews()).thenReturn(Arrays.asList(review));
        when(modelMapper.map(any(Review.class), eq(ReviewResponseDTO.class))).thenReturn(reviewResponseDTO);

        List<ReviewResponseDTO> reviews = reviewService.getRecentReviews();

        assertNotNull(reviews);
        assertEquals(1, reviews.size());
    }

    @Test
    public void testUpdateReview() {
        when(reviewRepository.findById(1)).thenReturn(Optional.of(review));
        when(modelMapper.map(any(ReviewRequestDTO.class), eq(Review.class))).thenReturn(review);
        when(reviewRepository.save(any(Review.class))).thenReturn(review);
        when(modelMapper.map(any(Review.class), eq(ReviewResponseDTO.class))).thenReturn(reviewResponseDTO);

        ReviewResponseDTO response = reviewService.updateReview(1, reviewRequestDTO);

        assertNotNull(response);
        assertEquals("Great stay!", response.getComment());
    }

    @Test
    public void testUpdateReview_NotFound() {
        when(reviewRepository.findById(1)).thenReturn(Optional.empty());

        ReviewException exception = assertThrows(ReviewException.class, () -> reviewService.updateReview(1, reviewRequestDTO));

        assertEquals("UPDTFAILS", exception.getCode());
        assertEquals("Review doesn't exist", exception.getMessage());
    }

    @Test
    public void testDeleteReview() {
        when(reviewRepository.findById(1)).thenReturn(Optional.of(review));
        doNothing().when(reviewRepository).deleteById(1);

        boolean isDeleted = reviewService.deleteByReviewId(1);

        assertTrue(isDeleted);
    }

    @Test
    public void testDeleteReview_NotFound() {
        when(reviewRepository.findById(1)).thenReturn(Optional.empty());

        ReviewException exception = assertThrows(ReviewException.class, () -> reviewService.deleteByReviewId(1));

        assertEquals("DLTFAILS", exception.getCode());
        assertEquals("Review doesn't exist", exception.getMessage());
    }
}
