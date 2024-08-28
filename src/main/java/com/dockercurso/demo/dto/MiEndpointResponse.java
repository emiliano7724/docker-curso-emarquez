package com.dockercurso.demo.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class MiEndpointResponse {
     String recibido;
     String status;
     String message;
}
