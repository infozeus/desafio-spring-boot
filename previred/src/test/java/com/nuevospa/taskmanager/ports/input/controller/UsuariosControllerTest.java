package com.nuevospa.taskmanager.ports.input.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.nuevospa.taskmanager.domain.dto.UsuarioDTO;
import com.nuevospa.taskmanager.domain.model.Usuarios;
import com.nuevospa.taskmanager.domain.repository.UsuariosRepository;

class UsuariosControllerTest {

     @Mock
    private UsuariosRepository usuariosRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UsuariosController usuariosController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
void saveUsuario_EncryptsPasswordAndSavesUser() {
    // Arrange
    UsuarioDTO userDTO = new UsuarioDTO();
    userDTO.setUser("testUser");
    userDTO.setPass("plainPassword");

    when(bCryptPasswordEncoder.encode("plainPassword")).thenReturn("encryptedPassword");

    // Act
    usuariosController.saveUsuario(userDTO);

    // Assert
    ArgumentCaptor<Usuarios> captor = ArgumentCaptor.forClass(Usuarios.class);
    verify(usuariosRepository, times(1)).save(captor.capture());
    Usuarios savedUser = captor.getValue();

    assertNotNull(savedUser);
    assertEquals("testUser", savedUser.getUsername());
    assertEquals("encryptedPassword", savedUser.getPassword());
    verify(bCryptPasswordEncoder, times(1)).encode("plainPassword");
}

    @Test
    void getAllUsuarios_ReturnsListOfUsers() {
        // Arrange
        Usuarios user1 = new Usuarios();
        user1.setUsername("user1");

        Usuarios user2 = new Usuarios();
        user2.setUsername("user2");

        when(usuariosRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        // Act
        List<UsuarioDTO> result = usuariosController.getAllUsuarios();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("user1", result.get(0).getUser());
        assertEquals("user2", result.get(1).getUser());
        verify(usuariosRepository, times(1)).findAll();
    }

    @Test
    void getUsuario_UserExists_ReturnsUser() {
        // Arrange
        String username = "testUser";
        Usuarios user = new Usuarios();
        user.setUsername(username);

        when(usuariosRepository.findByUsername(username)).thenReturn(user);

        // Act
        ResponseEntity<UsuarioDTO> response = usuariosController.getUsuario(username);
        UsuarioDTO result = response.getBody();

        // Assert
        assertNotNull(result);
        assertEquals(username, result.getUser());
        verify(usuariosRepository, times(1)).findByUsername(username);
    }

@Test
void getUsuario_UserDoesNotExist_ReturnsNull() {
    // Arrange
    String username = "nonExistentUser";

    when(usuariosRepository.findByUsername(username)).thenReturn(null);

    // Act
    ResponseEntity<UsuarioDTO> response = usuariosController.getUsuario(username);

    // Assert
    assertNotNull(response);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertNull(response.getBody());
    verify(usuariosRepository, times(1)).findByUsername(username);
}

}
