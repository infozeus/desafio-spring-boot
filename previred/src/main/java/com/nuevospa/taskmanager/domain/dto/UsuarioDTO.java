package com.nuevospa.taskmanager.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioDTO {
    public UsuarioDTO(String username, String password) {
        this.user = username;
        this.pass = password;
    }

    
    private String user;
    
    private String pass;

    private String token;

}
