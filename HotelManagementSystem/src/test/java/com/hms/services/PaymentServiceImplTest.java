package com.hms.services;

import com.hms.dto.PaymentRequestDTO;
import com.hms.dto.PaymentResponseDTO;
import com.hms.execption.PaymentException;
import com.hms.model.Payment;
import com.hms.model.Reservation;
import com.hms.repository.PaymentRepository;
import com.hms.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceImplTest {

    @InjectMocks
    private PaymentServiceImpl paymentServiceImpl;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ModelMapper modelMapper;

    private PaymentRequestDTO paymentRequestDTO;
    private PaymentResponseDTO paymentResponseDTO;
    private Payment payment;
    private Reservation reservation;

    @BeforeEach
    public void setUp() {
        reservation = new Reservation();
        reservation.setReservationId(1);

        paymentRequestDTO = new PaymentRequestDTO();
        paymentRequestDTO.setAmount(200);
        paymentRequestDTO.setPaymentDate(LocalDate.now());
        paymentRequestDTO.setPaymentStatus("Completed");
        paymentRequestDTO.setReservationId(1);

        paymentResponseDTO = new PaymentResponseDTO(1, 200, LocalDate.now(), "Completed", 1);

        payment = new Payment();
        payment.setPaymentId(1);
        payment.setAmount(200);
        payment.setPaymentDate(LocalDate.now());
        payment.setPaymentStatus("Completed");
        payment.setReservation(reservation);
    }

    @Test
    public void testFindAllPayments_Success() {
        when(paymentRepository.findAll()).thenReturn(Arrays.asList(payment));
        when(modelMapper.map(payment, PaymentResponseDTO.class)).thenReturn(paymentResponseDTO);

        List<PaymentResponseDTO> response = paymentServiceImpl.findAll();

        assertEquals(1, response.size());
        assertEquals(paymentResponseDTO, response.get(0));
        verify(paymentRepository, times(1)).findAll();
    }

    @Test
    public void testFindAllPayments_EmptyList() {
        when(paymentRepository.findAll()).thenReturn(Arrays.asList());

        PaymentException thrown = assertThrows(PaymentException.class, () -> paymentServiceImpl.findAll());
        assertEquals("Payment list is empty", thrown.getMessage());

        verify(paymentRepository, times(1)).findAll();
    }

    @Test
    public void testAddPaymentRecord_Success() {
        when(reservationRepository.findById(paymentRequestDTO.getReservationId())).thenReturn(Optional.of(reservation));
        when(modelMapper.map(paymentRequestDTO, Payment.class)).thenReturn(payment);
        when(paymentRepository.save(payment)).thenReturn(payment);
        when(modelMapper.map(payment, PaymentResponseDTO.class)).thenReturn(paymentResponseDTO);

        PaymentResponseDTO response = paymentServiceImpl.addPaymentRecord(paymentRequestDTO);

        assertNotNull(response);
        assertEquals(1, response.getReservationId());

        verify(reservationRepository, times(1)).findById(paymentRequestDTO.getReservationId());
        verify(paymentRepository, times(1)).save(payment);
    }

    @Test
    public void testAddPaymentRecord_ReservationNotFound() {
        when(reservationRepository.findById(paymentRequestDTO.getReservationId())).thenReturn(Optional.empty());

        PaymentException thrown = assertThrows(PaymentException.class, () -> paymentServiceImpl.addPaymentRecord(paymentRequestDTO));
        assertEquals("Reservation not found", thrown.getMessage());

        verify(reservationRepository, times(1)).findById(paymentRequestDTO.getReservationId());
        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    public void testFindByPaymentId_Success() {
        when(paymentRepository.findById(1)).thenReturn(Optional.of(payment));
        when(modelMapper.map(payment, PaymentResponseDTO.class)).thenReturn(paymentResponseDTO);

        PaymentResponseDTO response = paymentServiceImpl.findBypaymentId(1);

        assertNotNull(response);
        assertEquals(1, response.getPaymentId());

        verify(paymentRepository, times(1)).findById(1);
    }

    @Test
    public void testFindByPaymentId_PaymentNotFound() {
        when(paymentRepository.findById(1)).thenReturn(Optional.empty());

        PaymentException thrown = assertThrows(PaymentException.class, () -> paymentServiceImpl.findBypaymentId(1));
        assertEquals("Payment doesn't exist", thrown.getMessage());

        verify(paymentRepository, times(1)).findById(1);
    }

    @Test
    public void testDeleteByPaymentId_Success() {
        when(paymentRepository.findById(1)).thenReturn(Optional.of(payment));

        boolean isDeleted = paymentServiceImpl.deleteBypaymentId(1);

        assertTrue(isDeleted);
        verify(paymentRepository, times(1)).findById(1);
        verify(paymentRepository, times(1)).deleteById(1);
    }

    @Test
    public void testDeleteByPaymentId_PaymentNotFound() {
        when(paymentRepository.findById(1)).thenReturn(Optional.empty());

        PaymentException thrown = assertThrows(PaymentException.class, () -> paymentServiceImpl.deleteBypaymentId(1));
        assertEquals("Payment doesn't exist", thrown.getMessage());

        verify(paymentRepository, times(1)).findById(1);
        verify(paymentRepository, never()).deleteById(anyInt());
    }

    @Test
    public void testGetPaymentsByPaymentStatus_Success() {
        when(paymentRepository.findByPaymentStatus("Completed")).thenReturn(Arrays.asList(payment));
        when(modelMapper.map(payment, PaymentResponseDTO.class)).thenReturn(paymentResponseDTO);

        List<PaymentResponseDTO> response = paymentServiceImpl.getPaymentsByPaymentStatus("Completed");

        assertEquals(1, response.size());
        assertEquals(paymentResponseDTO, response.get(0));

        verify(paymentRepository, times(1)).findByPaymentStatus("Completed");
    }

    @Test
    public void testGetPaymentsByPaymentStatus_EmptyList() {
        when(paymentRepository.findByPaymentStatus("Completed")).thenReturn(Arrays.asList());

        PaymentException thrown = assertThrows(PaymentException.class, () -> paymentServiceImpl.getPaymentsByPaymentStatus("Completed"));
        assertEquals("Payment list is empty", thrown.getMessage());

        verify(paymentRepository, times(1)).findByPaymentStatus("Completed");
    }

    @Test
    public void testGetTotalRevenue() {
        double expectedRevenue = 5000.0;
        when(paymentRepository.getTotalRevenue()).thenReturn(expectedRevenue);

        double revenue = paymentServiceImpl.getTotalRevenue();

        assertEquals(expectedRevenue, revenue, 0.001);
        verify(paymentRepository, times(1)).getTotalRevenue();
    }
}
