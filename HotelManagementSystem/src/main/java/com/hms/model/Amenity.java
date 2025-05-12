package com.hms.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Amenity {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "amenity_id")
    private Integer amenityId;

    @NotBlank(message = "Name must not be blank")
    @Size(max = 100, message = "Name should not exceed 100 characters")
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank(message = "Description must not be blank")
    @Size(max = 500, message = "Description should not exceed 500 characters")
    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String description;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
        name = "hotelAmenity", 
        joinColumns = @JoinColumn(name = "amenity_id"), 
        inverseJoinColumns = @JoinColumn(name = "hotel_id") 
    )
    @JsonIgnore 
    private List<Hotel> hotels = new ArrayList<>();

    // Many-to-many relationship with Room
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
        name = "roomAmenity", 
        joinColumns = @JoinColumn(name = "amenity_id"), 
        inverseJoinColumns = @JoinColumn(name = "room_Id") 
    )
    @JsonIgnore 
    private List<Room> rooms = new ArrayList<>();


}
