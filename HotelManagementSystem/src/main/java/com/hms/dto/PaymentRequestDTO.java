package com.hms.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class PaymentRequestDTO {
	private int paymentId;
	
	@Min(value = 1, message = "Amount must be greater than 0")
	private int amount;
	
	@NotNull(message = "Payment date cannot be null")
	private LocalDate paymentDate;
	
	@NotBlank(message = "Payment status cannot be blank")
	@Size(min = 2, max = 30, message = "Payment status must be between 2 and 30 characters")
	private String paymentStatus;
	
	@Min(value = 1, message = "Reservation ID must be greater than 0")
	private int reservationId;

}
