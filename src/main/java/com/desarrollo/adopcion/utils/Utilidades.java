package com.desarrollo.adopcion.utils;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.desarrollo.adopcion.DTO.PetDto;
import com.desarrollo.adopcion.modelo.Pet;

@Component
public class Utilidades {
	
	public PetDto convertToDto(Pet pet) throws SQLException {
        PetDto petDto = new PetDto();
        petDto.setId(pet.getId());
        petDto.setNombre(pet.getNombre());
        petDto.setEspecie(pet.getEspecie());
        petDto.setRaza(pet.getRaza());
        petDto.setEdad(pet.getEdad());
        petDto.setGenero(pet.getGenero());
        petDto.setTamanio(pet.getTamanio());
        petDto.setDescripcion(pet.getDescripcion());
        
        Blob fotoBlob = pet.getFotoPerfil();
        if(fotoBlob != null) {
        	petDto.setFotoPerfil(fotoBlob.getBytes(1, (int) fotoBlob.length()));
        }
        petDto.setPhotos(pet.getPhotos().stream()
                             .map(foto -> {
                         		try {
                        			Blob blob = foto.getPhoto();
                        			return blob.getBytes(1, (int) blob.length());
                        		}catch (SQLException e) {
                        			throw new RuntimeException(e);
                        		}
                        	})
                             .collect(Collectors.toList()));
        return petDto;
    }

}
