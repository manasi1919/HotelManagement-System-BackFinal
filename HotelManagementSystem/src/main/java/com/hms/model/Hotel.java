package com.hms.model;

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
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Hotel {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hotel_id")
    private Integer hotelId;
 
    @NotBlank(message = "Hotel name is mandatory")
    @Size(max = 100, message = "Hotel name must not exceed 100 characters")
    @Column(name = "name", nullable = false)
    private String name;
 
    @NotBlank(message = "Location is mandatory")
    @Size(max = 200, message = "Location must not exceed 200 characters")
    @Column(name = "location", nullable = false)
    private String location;
 
    @NotBlank(message = "Description is mandatory")
    @Size(max = 500, message = "Description must not exceed 500 characters")
    @Column(name = "description", nullable = false)
    private String description;
 
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
        name = "hotelAmenity",
        joinColumns = @JoinColumn(name = "hotel_id"),
        inverseJoinColumns = @JoinColumn(name = "amenity_id")
    )
    @JsonIgnore
    private List<Amenity> amenities;
 
   
}
