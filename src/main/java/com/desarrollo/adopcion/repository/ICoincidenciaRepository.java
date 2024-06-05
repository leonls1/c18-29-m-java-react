package com.desarrollo.adopcion.repository;

import com.desarrollo.adopcion.modelo.Coincidencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ICoincidenciaRepository extends JpaRepository<Coincidencia, Long>{

}
