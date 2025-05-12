package com.hms.model;

import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reservationId;
    
    @Column(nullable = false)
    private String guestName;
 
    @Column(nullable = false)
    private String guestEmail;
 
    @Column(nullable = false)
    private String guestPhone;

    @Column(nullable = false)
    private LocalDate checkInDate;
 
    @Column(nullable = false)
    private LocalDate checkOutDate;
    
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

}
