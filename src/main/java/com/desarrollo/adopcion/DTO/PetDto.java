package com.desarrollo.adopcion.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetDto {

	private Long id;
	private String nombre;
	private String especie;
	private String raza;
	private int edad;
	private String genero;
	private String tamanio;
	private String descripcion;
	private byte[] fotoPerfil;
	private List<byte[]> photos;
	
}
