package com.desarrollo.adopcion.controller;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.desarrollo.adopcion.modelo.User;
import com.desarrollo.adopcion.service.IUserService;
import com.desarrollo.adopcion.service.UserService;

import com.desarrollo.adopcion.DTO.UserDto;
import com.desarrollo.adopcion.exception.UserException;
import com.desarrollo.adopcion.request.MessageRequest;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@CrossOrigin(origins = "*", maxAge=3600)
@RequestMapping("/api/users")
public class UserController {
	
	@Autowired
	private IUserService IuserService;
	@Autowired
	private UserService userService;
	
	
	@GetMapping("/todos")
	public ResponseEntity<List<User>> getAllUsers(){
		return new ResponseEntity<>(IuserService.getUsers(),HttpStatus.FOUND);
	}
	
	@GetMapping("/{correo}")
	public ResponseEntity<?> getUserByEmail(@PathVariable("correo") String correo) {
		try {
			User user = IuserService.getUserByCorreo(correo);
			UserDto userDto = convertToDto(user);
			return ResponseEntity.ok(userDto);
		}catch(UserException ex){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error recuperando el usuario");
		}
	}
	
	@PutMapping("/actualizar/{correo}")
	public ResponseEntity<String> actualizaUsuario(@PathVariable("correo") String correo, 
			@RequestParam("nombre") String nombre,
            @RequestParam("apellido") String apellido,
            @RequestParam("ubicacion") String ubicacion,
            @RequestParam(required=false) MultipartFile fotoPerfil
            ) 
{
		try {
			User userToModify = userService.getUserByCorreo(correo);
			userToModify.setNombre(nombre);
			userToModify.setApellido(apellido);
			userToModify.setUbicacion(ubicacion);
			if(!fotoPerfil.isEmpty()) {
    			byte[] fotoBytes;
    			try {
    				fotoBytes = fotoPerfil.getBytes();
    				Blob fotoBlob = new SerialBlob(fotoBytes);
    				userToModify.setFotoPerfil(fotoBlob);
    			} catch (IOException | SQLException e) {
    				e.printStackTrace();
    			}
    		}else {
    			userToModify.setFotoPerfil(null);
    		}
			userService.updateUser(correo, userToModify);
			return ResponseEntity.ok("Usuario actualizado con exito");
		}catch(UserException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error actualizando el usuario");
		}
	}
	
	@DeleteMapping("/eliminar/{correo}")
	public ResponseEntity<String> deleteUser(@PathVariable String correo) {
		try {
			IuserService.deleteUser(correo);
			return ResponseEntity.ok("Usuario Eliminado con exito");
		}catch(UserException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error eliminando el usuario");
		}
	}
        
        @Autowired
        private IMessageRepository 
        
        @PostMapping("/send")
        public ResponseEntity<String> sendMessage(@RequestBody MessageRequest request){
            
        }
	
	private UserDto convertToDto(User user) throws SQLException {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setNombre(user.getNombre());
        userDto.setApellido(user.getApellido());
        userDto.setCorreo(user.getCorreo());
        userDto.setUbicacion(user.getUbicacion());
        
        Blob fotoBlob = user.getFotoPerfil();
        if(fotoBlob != null) {
        	userDto.setFotoPerfil(fotoBlob.getBytes(1, (int) fotoBlob.length()));
        }
        
        return userDto;
    }
	
}
