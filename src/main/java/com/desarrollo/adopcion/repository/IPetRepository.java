package com.desarrollo.adopcion.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.desarrollo.adopcion.modelo.Pet;
import com.desarrollo.adopcion.modelo.User;

public interface IPetRepository extends JpaRepository<Pet, Long>{
	
	boolean existsByNombre(String name);
	
	Optional<Pet> findByNombreAndUser(String nombre, User user);
	
	Optional<Pet> findByNombre(String name);
	
	List<Pet> findByUser(Optional<User> currentUser);
}
