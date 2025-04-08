package com.nuevospa.taskmanager.ports.input.controller;

import com.nuevospa.taskmanager.application.service.impl.UsuarioDetailsServiceImpl;
import com.nuevospa.taskmanager.domain.dto.UsuarioDTO;
import com.nuevospa.taskmanager.domain.model.Usuarios;
import com.nuevospa.taskmanager.ports.input.security.jwt.JWTAuthenticationConfig;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Login Controller", description = "Operaciones relacionadas con el login.")
@RestController
public class LoginController {

    @Autowired
    JWTAuthenticationConfig jwtAuthenticationConfig;

    @Autowired
    UsuarioDetailsServiceImpl usuarioDetailsServiceImpl;

     @Operation(
        summary = "User login",
        description = "Authenticates a user and returns a JWT token.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Successful login",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UsuarioDTO.class)
                )
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Invalid username or password",
                content = @Content
            )
        }
    )
    @PostMapping("login")
    public UsuarioDTO login(
            @RequestParam("user") String username,
            @RequestParam("pass") String pass) {

        Usuarios usuario = usuarioDetailsServiceImpl.findByUsernameAndPassword(username, pass);

        if (usuario == null) {
            return null;
        }
        
        String token = jwtAuthenticationConfig.getJWTToken(username);
        return new UsuarioDTO (username, pass ,token);

    }

}
