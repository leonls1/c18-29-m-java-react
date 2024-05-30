package com.desarrollo.adopcion.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.desarrollo.adopcion.modelo.User;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
	
	boolean existsByCorreo(String correo);
	
	Optional<User> findByCorreo(String correo);
	
	void deleteByCorreo(String correo);
}
