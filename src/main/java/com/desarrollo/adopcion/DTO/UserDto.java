package com.desarrollo.adopcion.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
	
	private Long id;
	private String nombre;
	private String apellido;
	private String correo;
	private String clave;
	private String ubicacion;
	private byte[] fotoPerfil;
	
	public UserDto(String nombre, String apellido, String correo, String ubicacion, byte[] fotoPerfil) {
		this.nombre = nombre;
		this.apellido = apellido;
		this.correo = correo;
		this.ubicacion = ubicacion;
		this.fotoPerfil = fotoPerfil;
	}

}
