package com.desarrollo.adopcion.controller;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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

import com.desarrollo.adopcion.DTO.PetDto;
import com.desarrollo.adopcion.exception.ResourceNotFoundException;
import com.desarrollo.adopcion.modelo.IntencionLike;
import com.desarrollo.adopcion.modelo.Pet;
import com.desarrollo.adopcion.modelo.PetPhotos;
import com.desarrollo.adopcion.modelo.User;
import com.desarrollo.adopcion.modelo.Coincidencia;
import com.desarrollo.adopcion.repository.ICoincidenciaRepository;
import com.desarrollo.adopcion.repository.ILikeIntentionRepository;
import com.desarrollo.adopcion.repository.IPetPhotoRepository;
import com.desarrollo.adopcion.repository.IPetRepository;
import com.desarrollo.adopcion.repository.IUserRepository;
import com.desarrollo.adopcion.request.LikeRequest;
import com.desarrollo.adopcion.service.EmailService;
import com.desarrollo.adopcion.service.PetService;
import com.desarrollo.adopcion.utils.Utilidades;

@RestController
@CrossOrigin(origins="*", maxAge=3600)
@RequestMapping("/api/pets")
public class PetController {
	
	@Autowired
    public PetService petService;
	@Autowired
	public IPetRepository petRepository;
	@Autowired
	public IPetPhotoRepository petFotoRepository;
	@Autowired
	public IUserRepository userRepository;
	@Autowired
	public ILikeIntentionRepository intentionRepository;
	@Autowired
	public ICoincidenciaRepository coincidenciaRepository;
	@Autowired
    private EmailService emailService;
	@Autowired
	private Utilidades utilidades;

    @PostMapping("/crear/{userId}")

    public ResponseEntity<String> createPet(@PathVariable("userId") String userId, 
    		@RequestParam("nombre") String nombre,
            @RequestParam("especie") String especie,
            @RequestParam("raza") String raza,
            @RequestParam("edad") int edad,
            @RequestParam("genero") String genero,
            @RequestParam("tamanio") String tamanio,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("fotoPerfil") MultipartFile fotoPerfil, 
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
    		if(!fotoPerfil.isEmpty()) {
    			byte[] fotoBytes;
    			try {
    				fotoBytes = fotoPerfil.getBytes();
    				Blob fotoBlob = new SerialBlob(fotoBytes);
    				pet.setFotoPerfil(fotoBlob);
    			} catch (IOException | SQLException e) {
    				e.printStackTrace();
    			}
    		}
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
     
    @GetMapping("/user")
    public ResponseEntity<List<PetDto>> getPetsByUser(Authentication authentication)  {
    	Optional<User> currentUser = userRepository.findByCorreo(authentication.getName());
    	List<Pet> pets = petRepository.findByUser(currentUser);
    	List<PetDto> petDtos = pets.stream().map(t -> {
			try {
				return utilidades.convertToDto(t);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return null;
		}).collect(Collectors.toList());
        return ResponseEntity.ok(petDtos);
    }
    
    @GetMapping("/todas")
    public ResponseEntity<List<PetDto>> getAllPets() {
    	List<Pet> pets = petRepository.findAll();
    	List<PetDto> petDtos = pets.stream().map(t -> {
			try {
				return utilidades.convertToDto(t);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return null;
		}).collect(Collectors.toList());
        return ResponseEntity.ok(petDtos);
    }
    
    
    
    /*
    @GetMapping("/photos/{petId}")
    public ResponseEntity<List<byte[]>> getPhotos(@PathVariable Long petId) throws SQLException{
    	List<PetPhotos> fotos = petFotoRepository.findByPetId(petId);
    	List<byte[]> photos = fotos.stream().map(foto -> {
    		try {
    			Blob blob = foto.getPhoto();
    			return blob.getBytes(1, (int) blob.length());
    		}catch (SQLException e) {
    			throw new RuntimeException(e);
    		}
    	}).collect(Collectors.toList());
    	return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(photos);
    	
    }
     */
    
    @PostMapping("/likes")
    public ResponseEntity<?> likePet(@RequestBody LikeRequest request, Authentication authentication) throws ResourceNotFoundException {
    	System.out.println("Datos Recibidos "+request.getPetId()+" "+authentication.getName());
    	User currentUser = userRepository.findByCorreo(authentication.getName()).orElseThrow(); // Capturo usuario
        Pet fromPet = currentUser.getPets().get(0); // Capturo las mascotas del usuario que dió like
        Pet toPet = petRepository.findById(request.getPetId()).orElseThrow(() -> new ResourceNotFoundException("Pet not found")); // Busco la mascota a la que se dió like 
        User petOwner = toPet.getUser(); // Se busca propietario de mascota con like 

        // Chequea si ya existe intencion de la otra mascota
        Optional<IntencionLike> existingIntention = intentionRepository.findByFromPetAndToPet(toPet, fromPet);
        if (existingIntention.isPresent()) {
            // Crea el match en la base de datos
            Coincidencia coincidencia = new Coincidencia();
            coincidencia.setPet1(fromPet);
            coincidencia.setPet2(toPet);
            coincidencia.setFecha_match(LocalDateTime.now());
            coincidenciaRepository.save(coincidencia);

            // Elimina la intención existente
            intentionRepository.delete(existingIntention.get());

            // Envía notificacion a ambos usuarios
            emailService.sendMatchNotification(currentUser.getCorreo(), petOwner.getCorreo(), fromPet, toPet);
            return ResponseEntity.ok("Match created and notifications sent");
        } else {
            // Si no habia intención previa, se graba la intencion
        	IntencionLike likeIntention = new IntencionLike(fromPet, toPet);
            intentionRepository.save(likeIntention);

            // Envía notificacion al propietario de la mascota 
            emailService.sendLikeNotification(petOwner.getCorreo(), currentUser, fromPet, toPet);
    	return ResponseEntity.ok("Like registrado y correo enviado");
        }
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
