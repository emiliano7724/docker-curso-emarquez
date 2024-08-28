package com.dockercurso.demo.services;

import com.dockercurso.demo.dto.MiEndpointResponse;

public interface MiEndpointServiceI {
    MiEndpointResponse procesarParametro(String abc);
}
