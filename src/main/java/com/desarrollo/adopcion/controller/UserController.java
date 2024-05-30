package com.desarrollo.adopcion.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.desarrollo.adopcion.modelo.User;
import com.desarrollo.adopcion.service.IUserService;

import com.desarrollo.adopcion.exception.UserException;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
	@Autowired
	private IUserService IuserService;
	
	
	@GetMapping("/todos")
	public ResponseEntity<List<User>> getAllUsers(){
		return new ResponseEntity<>(IuserService.getUsers(),HttpStatus.FOUND);
	}
	
	@GetMapping("/{correo}")
	public ResponseEntity<?> getUserByEmail(@PathVariable("correo") String correo) {
		try {
			User user = IuserService.getUserByCorreo(correo);
			return ResponseEntity.ok(user);
		}catch(UserException ex){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error recuperando el usuario");
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
	
}
