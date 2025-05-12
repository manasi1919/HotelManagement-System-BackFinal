package com.hms.services;

import java.util.List;

import com.hms.dto.PaymentRequestDTO;
import com.hms.dto.PaymentResponseDTO;

public interface PaymentServiceIntf {
	 List<PaymentResponseDTO> findAll();
	    PaymentResponseDTO addPaymentRecord(PaymentRequestDTO paymentRequestDTO);
	    PaymentResponseDTO findBypaymentId(int paymentId);
	    boolean deleteBypaymentId(int paymentId);
	   
	    List<PaymentResponseDTO> getPaymentsByPaymentStatus(String status);
	    double getTotalRevenue();
	

}
