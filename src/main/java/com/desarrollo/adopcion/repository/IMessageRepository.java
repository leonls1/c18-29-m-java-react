package com.desarrollo.adopcion.repository;

import com.desarrollo.adopcion.modelo.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IMessageRepository extends JpaRepository<Message, Long>{

}
