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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
 
import com.hms.dto.PaymentRequestDTO;
import com.hms.dto.PaymentResponseDTO;
import com.hms.services.PaymentServiceIntf;
 
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
 
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/payment")
public class PaymentController {
 
    @Autowired
    private PaymentServiceIntf paymentServiceIntf;
 
    @Operation(summary="Create a new payment")
    @PostMapping("/post")
    public ResponseEntity<?> addPaymentRecord(@Valid @RequestBody PaymentRequestDTO paymentRequestDTO) {
    	try {
            paymentServiceIntf.addPaymentRecord(paymentRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("POSTSUCCESS: Payment added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("ADDFAILS: Payment already exists");
        }
    }
 
    @Operation(summary="Get a list of all payments.")
    @GetMapping("/all")
    public ResponseEntity<?> findAll() {
    	List<PaymentResponseDTO> payments = paymentServiceIntf.findAll();
        if (payments.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Map.of("code", "GETALLFAILS", "message", "Payment list is empty"));
        }
        return ResponseEntity.ok(payments); 
    }

 
    @Operation(summary="Get payment By Id")
    @GetMapping("/{paymentId}")
    public ResponseEntity<?> findBypaymentId(@PathVariable(name = "paymentId") int paymentId) {
        PaymentResponseDTO response = paymentServiceIntf.findBypaymentId(paymentId);
        if (response != null) {
            return ResponseEntity.ok(response); //  Returns a JSON object directly
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("code", "GETFAILS", "message", "Payment doesn't exist"));
        }
    }
 
    @Operation(summary="Retrieve payments based on their status")
    @GetMapping("/status/{status}")
    public ResponseEntity<Map<String, Object>> getPaymentsByStatus(@PathVariable(name = "status") String status) {
        List<PaymentResponseDTO> payments = paymentServiceIntf.getPaymentsByPaymentStatus(status);
        if (payments.isEmpty()) {
            return new ResponseEntity<>(
                Map.of("code", "GETALLFAILS", "message", "Payment list is empty"),
                HttpStatus.NOT_FOUND
            );
        }
        return new ResponseEntity<>(Map.of("data", payments), HttpStatus.OK);
    }
 
    @Operation(summary="Retrieve the total revenue generated through payments.")
    @GetMapping("/total-revenue")
    public ResponseEntity<Map<String, Object>> getTotalRevenue() {
        double revenue = paymentServiceIntf.getTotalRevenue();
        return new ResponseEntity<>(Map.of("totalRevenue", revenue), HttpStatus.OK);
    }
 
    @Operation(summary="Delete a payment.")
    @DeleteMapping("/{paymentId}")
    public ResponseEntity<Map<String, Object>> deleteBypaymentId(@PathVariable(name = "paymentId") int paymentId) {
        boolean deleted = paymentServiceIntf.deleteBypaymentId(paymentId);
        if (deleted) {
            return new ResponseEntity<>(
                Map.of("code", "DELETESUCCESS", "message", "Payment deleted successfully"),
                HttpStatus.OK
            );
        } else {
            return new ResponseEntity<>(
                Map.of("code", "DLTFAILS", "message", "Payment doesn't exist"),
                HttpStatus.NOT_FOUND
            );
        }
    }
}