package com.nuevospa.taskmanager.ports.input.controller;

import com.nuevospa.taskmanager.domain.dto.UsuarioDTO;
import com.nuevospa.taskmanager.domain.model.Usuarios;
import com.nuevospa.taskmanager.domain.repository.UsuariosRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Usuarios Controller", description = "Operaciones relacionadas con usuarios")
@RestController
public class UsuariosController {

    private UsuariosRepository usuariosRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UsuariosController(UsuariosRepository usuarioRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.usuariosRepository = usuarioRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Operation(
        summary = "Create a new user",
        description = "Save a new user with an encrypted password.",
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "User created successfully",
                content = @Content
            )
        }
    )
    @PostMapping("/users/")
    public void saveUsuario(@RequestBody UsuarioDTO user) {
        user.setPass(bCryptPasswordEncoder.encode(user.getPass()));
        Usuarios usuario = new Usuarios();
        usuario.setUsername(user.getUser());
        usuario.setPassword(user.getPass());
        usuariosRepository.save(usuario);
    }

    @Operation(
        summary = "Get all users",
        description = "Retrieve a list of all users.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "List of users retrieved successfully",
                content = @Content
            )
        }
    )
    @GetMapping("/users/all")
    public List<UsuarioDTO> getAllUsuarios() {
        return usuariosRepository.findAll().stream()
                .map(usuario -> new UsuarioDTO(usuario.getUsername(), usuario.getPassword()))
                .toList();
    }

    @Operation(
        summary = "Get user by username",
        description = "Retrieve a specific user by their username.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "User retrieved successfully",
                content = @Content
            )
        }
    )
    @GetMapping("/users/{username}")
    public ResponseEntity<UsuarioDTO> getUsuario(String username) {
    Usuarios user = usuariosRepository.findByUsername(username);
    if (user != null) {
        UsuarioDTO userDTO = new UsuarioDTO();
        userDTO.setUser(user.getUsername());
        return ResponseEntity.ok(userDTO);
    }
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
}

}
