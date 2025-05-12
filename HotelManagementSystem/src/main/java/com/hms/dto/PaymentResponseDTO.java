package com.hms.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponseDTO {
	private int paymentId;
	private int amount;
	private LocalDate paymentDate;
	private String paymentStatus;
	private int reservationId;

}
