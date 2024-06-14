package com.desarrollo.adopcion.Correo;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import com.desarrollo.adopcion.modelo.User;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClaveResetToken {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String token;
	
	@OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name="user_id")
	private User user;
	
	private Date fechaExpira;
	
	public ClaveResetToken(String token, User user) {
		this.user = user;
		this.token = token;
		this.fechaExpira = calculaFechaExpira(60*24);
		
	}

	private Date calculaFechaExpira(int expiraEnMinutos) {
		Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, expiraEnMinutos);
        return new Date(cal.getTime().getTime());
	}

}
