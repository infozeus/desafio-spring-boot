package com.nuevospa.taskmanager.application.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.nuevospa.taskmanager.domain.model.Usuarios;
import com.nuevospa.taskmanager.domain.repository.UsuariosRepository;

class UsuarioDetailsServiceImplTest {

    @Mock
    private UsuariosRepository usuariosRepository;

    @InjectMocks
    private UsuarioDetailsServiceImpl usuarioDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUserByUsername_UserExists_ReturnsUserDetails() {
        // Arrange
        String username = "testUser";
        Usuarios mockUsuario = new Usuarios();
        mockUsuario.setUsername(username);
        mockUsuario.setPassword("password");
        when(usuariosRepository.findByUsername(username)).thenReturn(mockUsuario);

        // Act
        UserDetails userDetails = usuarioDetailsService.loadUserByUsername(username);

        // Assert
        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        verify(usuariosRepository, times(1)).findByUsername(username);
    }

    @Test
    void loadUserByUsername_UserDoesNotExist_ThrowsUsernameNotFoundException() {
        // Arrange
        String username = "nonExistentUser";
        when(usuariosRepository.findByUsername(username)).thenReturn(null);

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> usuarioDetailsService.loadUserByUsername(username));
        verify(usuariosRepository, times(1)).findByUsername(username);
    }

    @Test
    void findByUsernameAndPassword_ValidCredentials_ReturnsUsuario() {
        // Arrange
        String username = "testUser";
        String password = "password";
        Usuarios mockUsuario = new Usuarios();
        mockUsuario.setUsername(username);
        mockUsuario.setPassword(password);
        when(usuariosRepository.findByUsernameAndPassword(username, password)).thenReturn(mockUsuario);

        // Act
        Usuarios usuario = usuarioDetailsService.findByUsernameAndPassword(username, password);

        // Assert
        assertNotNull(usuario);
        assertEquals(username, usuario.getUsername());
        assertEquals(password, usuario.getPassword());
        verify(usuariosRepository, times(1)).findByUsernameAndPassword(username, password);
    }

    @Test
    void findByUsernameAndPassword_InvalidCredentials_ReturnsNull() {
        // Arrange
        String username = "testUser";
        String password = "wrongPassword";
        when(usuariosRepository.findByUsernameAndPassword(username, password)).thenReturn(null);

        // Act
        Usuarios usuario = usuarioDetailsService.findByUsernameAndPassword(username, password);

        // Assert
        assertNull(usuario);
        verify(usuariosRepository, times(1)).findByUsernameAndPassword(username, password);
    }

}
