package com.dockercurso.demo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "registro_llamada")

public class RegistroLLamada {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String parametroRecibido;

    @CreationTimestamp
    @Column(name = "created_at",updatable = false)
    private LocalDateTime fechaCreacion;

}