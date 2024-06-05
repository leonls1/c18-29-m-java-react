package com.desarrollo.adopcion.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desarrollo.adopcion.exception.ResourceAlreadyExistsException;
import com.desarrollo.adopcion.exception.ResourceNotFoundException;
import com.desarrollo.adopcion.modelo.Pet;
import com.desarrollo.adopcion.modelo.User;
import com.desarrollo.adopcion.repository.IPetRepository;

@Service
public class PetService {
	
	@Autowired
    private IPetRepository iPetRepository;
	
	@Autowired
	private UserService userService;
	
    public Pet createPet(Pet pet, String userId) throws ResourceNotFoundException, ResourceAlreadyExistsException {
    	
    	User user = userService.getUserByCorreo(userId);
    	System.out.println("Llego al servicio");
    	/*
    	Optional<Pet> mascota = iPetRepository.findByNombreAndUser(pet.getNombre(), user);
    	System.out.println("Encontro el nombre de la mascota con el mismo usuario"+mascota.get().getNombre()+" "+mascota.get().getUser().getNombre());
        if(mascota != null){
            throw new ResourceAlreadyExistsException("Tienes una mascota con el mismo nombre");
        }*/
        pet.setCreadoEn(LocalDateTime.now());
        user.addPet(pet);
        return iPetRepository.save(pet);
    }

    public Optional<Pet> getPetById(Long id) throws ResourceNotFoundException {
        Optional<Pet> pet = iPetRepository.findById(id);
        if (pet == null) {
            throw new ResourceNotFoundException("Pet with id: " + id + "not found");
        }
        return pet;

    }

    public Optional<Pet> getPetByName(String name) throws ResourceNotFoundException{
       Optional<Pet> pet = iPetRepository.findByNombre(name);
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
