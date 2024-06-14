package com.desarrollo.adopcion.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class WebController {

	@GetMapping
	public String index() {
		System.out.println("Si llega al webController");
		return "forward:/static/index.html";
	}

}
