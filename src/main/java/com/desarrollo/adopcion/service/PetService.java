package com.desarrollo.adopcion.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desarrollo.adopcion.exception.ResourceAlreadyExistsException;
import com.desarrollo.adopcion.exception.ResourceNotFoundException;
import com.desarrollo.adopcion.modelo.Pet;
import com.desarrollo.adopcion.repository.IPetRepository;

@Service
public class PetService {
	
	@Autowired
    private IPetRepository iPetRepository;
	
    public Pet createPet(Pet pet) throws ResourceNotFoundException, ResourceAlreadyExistsException {
        if(iPetRepository.existsByNombre(pet.getNombre())){
            throw new ResourceAlreadyExistsException("A Pet already exists in the database");
        }
        pet.setCreadoEn(LocalDateTime.now());
        return iPetRepository.save(pet);
    }

    public Pet getPetById(Long id) throws ResourceNotFoundException {
        Pet pet = iPetRepository.findById(id).orElse(null);
        if (pet == null) {
            throw new ResourceNotFoundException("Pet with id: " + id + "not found");
        }
        return pet;

    }

    public Pet getPetByName(String name) throws ResourceNotFoundException{
       Pet pet = iPetRepository.findByNombre(name).orElse(null);
        if (pet== null) {
            throw new ResourceNotFoundException("Pet not found with id: " + name);
        }
        return pet;
    }

    public List<Pet> getAllPets() {
        return  iPetRepository.findAll();
    }
    
    public Pet updatePet(Pet pet) throws ResourceNotFoundException {
        Pet existingPet = iPetRepository.findById(pet.getId()).orElse(null);
        if (existingPet != null) {
            existingPet.setNombre(pet.getNombre());
            existingPet.setEdad(pet.getEdad());
            existingPet.setRaza(pet.getRaza());
            existingPet.setGenero(pet.getGenero());
            existingPet.setTamanio(pet.getTamanio());
            existingPet.setEspecie(pet.getEspecie());
            existingPet.setDescripcion(pet.getDescripcion());
            return iPetRepository.save(existingPet);
        } else {
            throw new ResourceNotFoundException("Pet with ID " + pet.getId() + " not found");
        }
    }

    public void deletePet(Long id) {
        iPetRepository.deleteById(id);
    }

}
