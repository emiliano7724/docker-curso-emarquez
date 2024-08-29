package com.dockercurso.demo.services.impl;

import com.dockercurso.demo.dto.MiEndpointResponse;
import com.dockercurso.demo.entities.RegistroLLamada;
import com.dockercurso.demo.repositories.MiEntidadRepository;
import com.dockercurso.demo.services.MiEndpointServiceI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MiEndpointService implements MiEndpointServiceI {


    private static final String PARAMETRO_ESPERADO = "123" ;
    final MiEntidadRepository miEntidadRepository;

    public MiEndpointResponse procesarParametro(String abc) {
        String recibido = abc != null ? abc : "Parámetro no proporcionado";
        String status;
        String message;

        if (PARAMETRO_ESPERADO.equals(abc)) {
            status = "OK";
            message = "Parámetro procesado correctamente, retorno: XYZ";
            miEntidadRepository.save(RegistroLLamada.builder().parametroRecibido(recibido).build());
            return MiEndpointResponse.builder()
                    .recibido(recibido)
                    .status(status)
                    .message(message)
                    .build();

        } else {
            miEntidadRepository.save(RegistroLLamada.builder().parametroRecibido(recibido).build());
            return procesarOtroParametro(abc);
        }
    }

    private MiEndpointResponse procesarOtroParametro(String parametro) {
        String status;
        String message;

        switch (parametro) {

            default:
                status = "ERROR";
                message = "Parámetro desconocido";
                break;
        }


        return MiEndpointResponse.builder()
                .recibido(parametro)
                .status(status)
                .message(message)
                .build();
    }
}


