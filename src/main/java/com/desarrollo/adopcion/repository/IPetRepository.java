package com.desarrollo.adopcion.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.desarrollo.adopcion.modelo.Pet;

public interface IPetRepository extends JpaRepository<Pet, Long>{
	boolean existsByNombre(String name);
	Optional<Pet> findByNombre(String name);
}
