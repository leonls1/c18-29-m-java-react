package com.desarrollo.adopcion.DTO;

import java.time.LocalDateTime;
import java.util.List;

import com.desarrollo.adopcion.modelo.Message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchDto {

	private Long id;
	private PetDto pet1;
	private PetDto pet2;
	private LocalDateTime fecha_match;
	private List<Message> messages;
	
	public MatchDto(PetDto pet1, PetDto pet2, LocalDateTime fecha) {
		this.pet1 = pet1;
		this.pet2 = pet2;
		this.fecha_match = fecha;
	}
}
