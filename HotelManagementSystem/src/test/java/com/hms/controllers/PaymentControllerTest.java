package com.hms.controllers;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hms.controller.PaymentController;
import com.hms.dto.PaymentRequestDTO;
import com.hms.dto.PaymentResponseDTO;
import com.hms.services.PaymentServiceIntf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PaymentServiceIntf paymentService;

    @InjectMocks
    private PaymentController paymentController;

    private final ObjectMapper objectMapper = new ObjectMapper();
    
    private PaymentRequestDTO paymentRequest;
    private PaymentResponseDTO paymentResponse;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(paymentController).build();

        // Initializing test objects
        paymentRequest = new PaymentRequestDTO();
        paymentResponse = new PaymentResponseDTO();
    }

    @Test
    void testAddPaymentRecord() throws Exception {
        // Arrange
//        doNothing().when(paymentService).addPaymentRecord(any(PaymentRequestDTO.class));
//
//        // Act & Assert
//        mockMvc.perform(post("/api/payment/post")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(paymentRequest)))
//                .andExpect(status().isCreated());
//
//        // Verify
//        verify(paymentService, times(1)).addPaymentRecord(any(PaymentRequestDTO.class));
    }

    @Test
    void testFindAllPayments() throws Exception {
        List<PaymentResponseDTO> payments = List.of(paymentResponse);
        when(paymentService.findAll()).thenReturn(payments);

        mockMvc.perform(get("/api/payment/all"))
                .andExpect(status().isOk());
    }

    @Test
    void testFindAllPayments_EmptyList() throws Exception {
        when(paymentService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/payment/all"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testFindByPaymentId() throws Exception {
        when(paymentService.findBypaymentId(1)).thenReturn(paymentResponse);

        mockMvc.perform(get("/api/payment/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testFindByPaymentId_NotFound() throws Exception {
        when(paymentService.findBypaymentId(1)).thenReturn(null);

        mockMvc.perform(get("/api/payment/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetPaymentsByStatus() throws Exception {
        List<PaymentResponseDTO> payments = List.of(paymentResponse);
        when(paymentService.getPaymentsByPaymentStatus("PAID")).thenReturn(payments);

        mockMvc.perform(get("/api/payment/status/PAID"))
                .andExpect(status().isOk());
    }



    @Test
    void testDeleteByPaymentId() throws Exception {
        when(paymentService.deleteBypaymentId(1)).thenReturn(true);

        mockMvc.perform(delete("/api/payment/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteByPaymentId_NotFound() throws Exception {
        when(paymentService.deleteBypaymentId(1)).thenReturn(false);

        mockMvc.perform(delete("/api/payment/1"))
                .andExpect(status().isNotFound());
    }
}
