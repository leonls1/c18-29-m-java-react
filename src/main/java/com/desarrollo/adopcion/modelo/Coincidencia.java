package com.desarrollo.adopcion.modelo;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Coincidencia {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "pet1_id")
	private Pet pet1;
	
	@ManyToOne
	@JoinColumn(name = "pet2_id")
	private Pet pet2;
	
	private LocalDateTime fecha_match;
	
	@OneToMany(mappedBy = "match", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Message> messages;

}
