package com.dockercurso.demo.controllers;

import com.dockercurso.demo.dto.MiEndpointResponse;
import com.dockercurso.demo.services.MiEndpointServiceI;
import com.dockercurso.demo.services.impl.MiEndpointService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class MiControlador {

    final MiEndpointServiceI miServicio;

    @GetMapping("/mi_endpoint")
    public MiEndpointResponse obtenerResultado(@RequestParam String abcParam) {
        return miServicio.procesarParametro(abcParam);
    }


}
