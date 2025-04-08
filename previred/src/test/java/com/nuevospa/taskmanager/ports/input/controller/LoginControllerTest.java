package com.nuevospa.taskmanager.ports.input.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.nuevospa.taskmanager.application.service.impl.UsuarioDetailsServiceImpl;
import com.nuevospa.taskmanager.domain.dto.UsuarioDTO;
import com.nuevospa.taskmanager.domain.model.Usuarios;
import com.nuevospa.taskmanager.ports.input.security.jwt.JWTAuthenticationConfig;

class LoginControllerTest {

    @Mock
    private JWTAuthenticationConfig jwtAuthenticationConfig;

    @Mock
    private UsuarioDetailsServiceImpl usuarioDetailsServiceImpl;

    @InjectMocks
    private LoginController loginController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login_ValidCredentials_ReturnsUsuarioDTO() {
        // Arrange
        String username = "testUser";
        String password = "testPass";
        String token = "mockToken";

        Usuarios mockUsuario = new Usuarios();
        mockUsuario.setUsername(username);
        mockUsuario.setPassword(password);

        when(usuarioDetailsServiceImpl.findByUsernameAndPassword(username, password)).thenReturn(mockUsuario);
        when(jwtAuthenticationConfig.getJWTToken(username)).thenReturn(token);

        // Act
        UsuarioDTO result = loginController.login(username, password);

        // Assert
        assertNotNull(result);
        assertEquals(username, result.getUser());
        assertEquals(password, result.getPass());
        assertEquals(token, result.getToken());
        verify(usuarioDetailsServiceImpl, times(1)).findByUsernameAndPassword(username, password);
        verify(jwtAuthenticationConfig, times(1)).getJWTToken(username);
    }

    @Test
    void login_InvalidCredentials_ReturnsNull() {
        // Arrange
        String username = "invalidUser";
        String password = "invalidPass";

        when(usuarioDetailsServiceImpl.findByUsernameAndPassword(username, password)).thenReturn(null);

        // Act
        UsuarioDTO result = loginController.login(username, password);

        // Assert
        assertNull(result);
        verify(usuarioDetailsServiceImpl, times(1)).findByUsernameAndPassword(username, password);
        verify(jwtAuthenticationConfig, never()).getJWTToken(anyString());
    }

}
