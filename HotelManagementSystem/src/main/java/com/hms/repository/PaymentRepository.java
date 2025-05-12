package com.hms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hms.model.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer>{
	   // Retrieve payments by status
    List<Payment> findByPaymentStatus(String status);

    // Calculate total revenue
    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p")
    double getTotalRevenue();
 //   List<Payment> findByReservationReservationId(int reservationId); // Fetch payments by reservation ID

}
