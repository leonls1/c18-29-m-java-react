package com.desarrollo.adopcion.Correo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClaveResetTokenRepository extends JpaRepository<ClaveResetToken, Long>{
	ClaveResetToken findByToken(String token);

}
