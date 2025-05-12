package com.hms.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hms.dto.PaymentRequestDTO;
import com.hms.dto.PaymentResponseDTO;
import com.hms.execption.PaymentException;
import com.hms.model.Payment;
import com.hms.model.Reservation;
import com.hms.repository.PaymentRepository;
import com.hms.repository.ReservationRepository;

import jakarta.transaction.Transactional;

@Service
public class PaymentServiceImpl implements PaymentServiceIntf {
	 @Autowired
	    private PaymentRepository paymentRepository;
	    
	    @Autowired
	    private ReservationRepository reservationRepository; // Fetch Reservation before saving payment
	    
	    @Autowired
	    private ModelMapper modelMapper;

	    @Override
	    public List<PaymentResponseDTO> findAll() {
	        List<Payment> payments = paymentRepository.findAll();
	        if (payments.isEmpty()) {
	            throw new PaymentException("Payment list is empty");
	        }
	        return payments.stream()
	                .map(payment -> modelMapper.map(payment, PaymentResponseDTO.class))
	                .collect(Collectors.toList());
	    }

	  /*  @Override
	    public PaymentResponseDTO addPaymentRecord(PaymentRequestDTO paymentRequestDTO) {
	        // Fetch the associated reservation
	        Reservation reservation = reservationRepository.findById(paymentRequestDTO.getReservationId())
	                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));

	        // Map DTO to entity
	        Payment payment = modelMapper.map(paymentRequestDTO, Payment.class);
	        payment.setReservationId(reservation.getReservationId()); // Set the relationship

	        // Save and return DTO
	        payment = paymentRepository.save(payment);
	        return modelMapper.map(payment, PaymentResponseDTO.class);
	    }*/
	    @Transactional
	    @Override
	    public PaymentResponseDTO addPaymentRecord(PaymentRequestDTO paymentRequestDTO) {
	        Reservation reservation = reservationRepository.findById(paymentRequestDTO.getReservationId())
	                .orElseThrow(() -> new PaymentException("Reservation not found"));
	        Payment payment = modelMapper.map(paymentRequestDTO, Payment.class);
	        payment.setReservation(reservation); // Set the reservation
	        payment = paymentRepository.save(payment);
	        PaymentResponseDTO responseDTO = modelMapper.map(payment, PaymentResponseDTO.class);
	        responseDTO.setReservationId(payment.getReservation().getReservationId()); // Ensure to set reservationId manually
	        
	        return responseDTO;
	    }


	    
	    @Override
	    public PaymentResponseDTO findBypaymentId(int paymentId) {
	        Payment payment = paymentRepository.findById(paymentId)
	                .orElseThrow(() -> new PaymentException("Payment doesn't exist"));
	        return modelMapper.map(payment, PaymentResponseDTO.class);
	    }

	    @Override
	    public boolean deleteBypaymentId(int paymentId) {
	        Payment payment = paymentRepository.findById(paymentId)
	                .orElseThrow(() -> new PaymentException("Payment doesn't exist"));
	        paymentRepository.deleteById(paymentId);
	        return true;
	    }

	    /* @Override
	    public PaymentResponseDTO updatePayment(int paymentId, PaymentRequestDTO paymentRequestDTO) {
	        Payment payment = paymentRepository.findById(paymentId)
	                .orElseThrow(() -> new PaymentNotFoundException("Payment doesn't exist"));
	        
	        modelMapper.map(paymentRequestDTO, payment);
	        payment = paymentRepository.save(payment);
	        return modelMapper.map(payment, PaymentResponseDTO.class);
	    } */

	    @Override
	    public List<PaymentResponseDTO> getPaymentsByPaymentStatus(String status) {
	        List<Payment> payments = paymentRepository.findByPaymentStatus(status);
	        if (payments.isEmpty()) {
	            throw new PaymentException("Payment list is empty");
	        }
	        return payments.stream()
	                .map(payment -> modelMapper.map(payment, PaymentResponseDTO.class))
	                .collect(Collectors.toList());
	    }

	    @Override
	    public double getTotalRevenue() {
	        return paymentRepository.getTotalRevenue();
	    }
	}