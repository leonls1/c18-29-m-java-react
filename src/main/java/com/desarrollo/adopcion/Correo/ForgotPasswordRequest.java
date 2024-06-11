package com.desarrollo.adopcion.Correo;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ForgotPasswordRequest {

	private String correo;
}
