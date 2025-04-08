package com.nuevospa.taskmanager.ports.input.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.nuevospa.taskmanager.application.service.impl.TareasServiceImpl;
import com.nuevospa.taskmanager.domain.dto.EstadoTareaDTO;
import com.nuevospa.taskmanager.domain.dto.TareaDTO;
import com.nuevospa.taskmanager.domain.model.EstadosTareas;
import com.nuevospa.taskmanager.domain.model.Tareas;

class TareasControllerTest {

    @Mock
    private TareasServiceImpl taskService;

    @InjectMocks
    private TareasController tareasController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllTasks_ReturnsListOfTasks() {
        // Arrange
        TareaDTO task1 = new TareaDTO();
        task1.setId(String.valueOf(1L));
        task1.setDescripcion("Task 1");
        task1.setEstado(null);

        TareaDTO task2 = new TareaDTO();
        task2.setId(String.valueOf(2L));
        task2.setDescripcion("Task 2");
        task2.setEstado(null);

        List<Tareas> tareasList = Arrays.asList(
                new Tareas(Long.valueOf(task1.getId()), task1.getDescripcion(), null),
                new Tareas(Long.valueOf(task2.getId()), task2.getDescripcion(), null)
        );
        when(taskService.getAllTasks()).thenReturn(tareasList);

        // Act
        ResponseEntity<List<TareaDTO>> response = tareasController.getAllTasks();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(taskService, times(1)).getAllTasks();
    }

    @Test
    void getTaskById_TaskExists_ReturnsTask() {
        // Arrange
        Long taskId = 1L;
        Tareas task = new Tareas();
        task.setId(taskId);
        task.setDescripcion("Task 1");

        when(taskService.getTaskById(taskId)).thenReturn(Optional.of(task));

        // Act
        ResponseEntity<TareaDTO> response = tareasController.getTaskById(taskId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(taskId.toString(), response.getBody().getId());
        verify(taskService, times(1)).getTaskById(taskId);
    }

    @Test
    void getTaskById_TaskDoesNotExist_ReturnsNotFound() {
        // Arrange
        Long taskId = 1L;
        when(taskService.getTaskById(taskId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<TareaDTO> response = tareasController.getTaskById(taskId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(taskService, times(1)).getTaskById(taskId);
    }

    @Test
void createTask_ReturnsCreatedTask() {
    // Arrange
    TareaDTO taskDTO = new TareaDTO();
    taskDTO.setDescripcion("New Task");
    EstadoTareaDTO estadoDTO = new EstadoTareaDTO();
    estadoDTO.setId("1");
    estadoDTO.setStatus("Pending");
    taskDTO.setEstado(estadoDTO);

    EstadosTareas estadoEntity = new EstadosTareas(1L, "Pending");
    Tareas taskEntity = new Tareas();
    taskEntity.setDescripcion(taskDTO.getDescripcion());
    taskEntity.setEstado(estadoEntity);

    Tareas createdTask = new Tareas();
    createdTask.setId(1L);
    createdTask.setDescripcion("New Task");
    createdTask.setEstado(estadoEntity);

    when(taskService.getEstadoById(1L)).thenReturn(Optional.of(estadoEntity));
    when(taskService.createTask(any(Tareas.class))).thenReturn(createdTask);

    // Act
    ResponseEntity<TareaDTO> response = tareasController.createTask(taskDTO);

    // Assert
    assertNotNull(response);
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(createdTask.getId().toString(), response.getBody().getId());
    assertEquals(createdTask.getDescripcion(), response.getBody().getDescripcion());
    assertNotNull(response.getBody().getEstado());
    assertEquals("1", response.getBody().getEstado().getId());
    assertEquals("Pending", response.getBody().getEstado().getStatus());
    verify(taskService, times(1)).getEstadoById(1L);
    verify(taskService, times(1)).createTask(any(Tareas.class));
}

@Test
void updateTask_TaskExists_ReturnsUpdatedTask() {
    // Arrange
    Long taskId = 1L;

    // Input DTO for the update
    TareaDTO taskDTO = new TareaDTO();
    taskDTO.setId(taskId.toString());
    taskDTO.setDescripcion("Updated Task");
    EstadoTareaDTO estadoDTO = new EstadoTareaDTO();
    estadoDTO.setId("1");
    estadoDTO.setStatus("In Progress");
    taskDTO.setEstado(estadoDTO);

    // Existing task in the database
    EstadosTareas existingEstado = new EstadosTareas(1L, "In Progress");
    Tareas existingTask = new Tareas();
    existingTask.setId(taskId);
    existingTask.setDescripcion("Old Task");
    existingTask.setEstado(existingEstado);

    // Updated task returned by the service
    EstadosTareas updatedEstado = new EstadosTareas(1L, "In Progress");
    Tareas updatedTask = new Tareas();
    updatedTask.setId(taskId);
    updatedTask.setDescripcion("Updated Task");
    updatedTask.setEstado(updatedEstado);

    when(taskService.getTaskById(taskId)).thenReturn(Optional.of(existingTask));
    when(taskService.updateTask(any(Tareas.class))).thenReturn(updatedTask);

    // Act
    ResponseEntity<TareaDTO> response = tareasController.updateTask(taskId, taskDTO);

    // Assert
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(taskId.toString(), response.getBody().getId());
    assertEquals("Updated Task", response.getBody().getDescripcion());
    assertNotNull(response.getBody().getEstado());
    assertEquals("1", response.getBody().getEstado().getId());
    assertEquals("In Progress", response.getBody().getEstado().getStatus());
    verify(taskService, times(1)).getTaskById(taskId);
    verify(taskService, times(1)).updateTask(any(Tareas.class));
}

    @Test
    void deleteTask_TaskExists_ReturnsNoContent() {
        // Arrange
        Long taskId = 1L;
        doNothing().when(taskService).deleteTask(taskId);

        // Act
        ResponseEntity<Void> response = tareasController.deleteTask(taskId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(taskService, times(1)).deleteTask(taskId);
    }

}
