package com.desarrollo.adopcion.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.desarrollo.adopcion.modelo.IntencionLike;
import com.desarrollo.adopcion.modelo.Pet;

public interface ILikeIntentionRepository extends JpaRepository<IntencionLike, Long>{
	Optional<IntencionLike> findByFromPetAndToPet(Pet fromPet, Pet toPet);
}
