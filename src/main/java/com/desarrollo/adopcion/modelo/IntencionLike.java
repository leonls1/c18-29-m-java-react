package com.desarrollo.adopcion.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IntencionLike {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Pet fromPet;

    @ManyToOne
    private Pet toPet;
    
    public IntencionLike(Pet fromPet, Pet toPet) {
        this.fromPet = fromPet;
        this.toPet = toPet;
    }
}
