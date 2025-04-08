package com.nuevospa.taskmanager.domain.dto;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstadoTareaDTO {

    private String id;
    private String status;

     // Override equals and hashCode
     @Override
     public boolean equals(Object o) {
         if (this == o) return true;
         if (o == null || getClass() != o.getClass()) return false;
         EstadoTareaDTO that = (EstadoTareaDTO) o;
         return Objects.equals(id, that.id) && Objects.equals(status, that.status);
     }
 
     @Override
     public int hashCode() {
         return Objects.hash(id, status);
     }
}
