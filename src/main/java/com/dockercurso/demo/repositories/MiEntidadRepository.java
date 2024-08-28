package com.dockercurso.demo.repositories;

import com.dockercurso.demo.entities.RegistroLLamada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MiEntidadRepository extends JpaRepository<RegistroLLamada, Long> {
}
