package com.desarrollo.adopcion.response;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {

	@NotBlank
    private String correo;
	@NotBlank
    private String token;
	@NotBlank
	private String role;

}
