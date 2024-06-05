package com.desarrollo.adopcion.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.desarrollo.adopcion.modelo.PetPhotos;

public interface IPetPhotoRepository extends JpaRepository<PetPhotos, Long>{
	List<PetPhotos> findByPetId(Long petId);
}
