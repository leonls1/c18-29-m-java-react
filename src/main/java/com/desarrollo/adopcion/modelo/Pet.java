package com.desarrollo.adopcion.modelo;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pet {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	
	private String nombre;
	
	private String especie;
	
	private String raza;
	
	private int edad;
	
	private String genero;
	
	private String tamanio;
	
	private String descripcion;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date creadoEn;
	
	@OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PetPhotos> photos;
	
	@OneToMany(mappedBy = "pet1", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Coincidencia> match1;
	
	@OneToMany(mappedBy = "pet2", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Coincidencia> match2;

}
