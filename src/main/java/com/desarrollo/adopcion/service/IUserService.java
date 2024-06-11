package com.desarrollo.adopcion.service;

import java.util.List;

import com.desarrollo.adopcion.modelo.User;

public interface IUserService {
	
	User saveUser(User user);
	
	List<User> getUsers();
	
	void deleteUser(String correo);
	
	User getUserByCorreo(String correo); 

}
