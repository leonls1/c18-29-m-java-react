package com.desarrollo.adopcion.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.desarrollo.adopcion.exception.ResourceNotFoundException;
import com.desarrollo.adopcion.modelo.Pet;
import com.desarrollo.adopcion.service.PetService;

@RestController
@RequestMapping("/api/pets")
public class PetController {
	
	@Autowired
    public PetService petService;

    @PostMapping("/crear")
    public ResponseEntity<String> createPet(@RequestBody Pet pet) {
    	try {
    		petService.createPet(pet);
    		return ResponseEntity.ok("Mascota creada con exito");    		
    	}catch(Exception e) {
    		return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    	}
    }

    @GetMapping
    public ResponseEntity<?> getPetById(@RequestParam(required = false) Long id,
    									@RequestParam(required = false) String nombre) {    	
    	try {
    		if(id!=null) {
    			Optional<Pet> pet = petService.getPetById(id);
    			return ResponseEntity.ok(pet);
    		}else if(nombre != null){
    			Optional<Pet> pet = petService.getPetByName(nombre);
    			return ResponseEntity.ok(pet);
    		}
    		return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Debe pasar un parametro");
    	}catch(ResourceNotFoundException ex){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error recuperando la mascota");
		}
    }
    
    
    @GetMapping("/todas")
    public ResponseEntity<List<Pet>> getAllPets()  {
        return new ResponseEntity<>(petService.getAllPets(), HttpStatus.FOUND);
    }

    @PutMapping("/actualizar")
    public ResponseEntity<String> updatePet(@RequestBody Pet pet) {
        try {
        	petService.updatePet(pet);    		
    		return ResponseEntity.ok("Mascota actualizada con exito");
    	}catch(ResourceNotFoundException ex){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error recuperando la mascota");
		}

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePet(@PathVariable Long id) throws ResourceNotFoundException {
    	try {
    		petService.deletePet(id);
    		return ResponseEntity.ok("Mascota eliminada con exito");	
    	}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error recuperando la mascota");
		}
    	
        
    }

}
