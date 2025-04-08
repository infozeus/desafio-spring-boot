package com.nuevospa.taskmanager.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TareaDTO {

    @JsonIgnore
    private String id;
    private String descripcion;
    private EstadoTareaDTO estado;

}
