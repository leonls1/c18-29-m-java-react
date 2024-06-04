package com.desarrollo.adopcion.controller;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.desarrollo.adopcion.exception.ResourceNotFoundException;
import com.desarrollo.adopcion.modelo.Pet;
import com.desarrollo.adopcion.modelo.PetPhotos;
import com.desarrollo.adopcion.repository.IPetPhotoRepository;
import com.desarrollo.adopcion.service.PetService;

@RestController
@CrossOrigin(origins="*", maxAge=3600)
@RequestMapping("/api/pets")
public class PetController {
	
	@Autowired
    public PetService petService;
	@Autowired
	public IPetPhotoRepository petFotoRepository;

    @PostMapping("/crear/{userId}")
    public ResponseEntity<String> createPet(@PathVariable("userId") String userId, 
    		@RequestParam("nombre") String nombre,
            @RequestParam("especie") String especie,
            @RequestParam("raza") String raza,
            @RequestParam("edad") int edad,
            @RequestParam("genero") String genero,
            @RequestParam("tamanio") String tamanio,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("photos") List<MultipartFile> photos) {
    	try {
    		Pet pet = new Pet();
    		
    		pet.setNombre(nombre);
    		pet.setEspecie(especie);
    		pet.setRaza(raza);
    		pet.setEdad(edad);
    		pet.setGenero(genero);
    		pet.setTamanio(tamanio);
    		pet.setDescripcion(descripcion);
    		pet.setCreadoEn(LocalDateTime.now());
    		
    		Pet petsaved = petService.createPet(pet, userId);
    		
            for (MultipartFile photo : photos) {
            	PetPhotos foto = new PetPhotos();
            	if(!photo.isEmpty()) {
        			byte[] fotoBytes;
        			try {
        				fotoBytes = photo.getBytes();
        				Blob fotoBlob = new SerialBlob(fotoBytes);
        				foto.setPhoto(fotoBlob);
        			} catch (IOException | SQLException e) {
        				e.printStackTrace();
        			}
        		}
            	foto.setPet(petsaved);
            	foto.setSubidaEn(LocalDateTime.now());
            	petFotoRepository.save(foto);
            }
    		
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
