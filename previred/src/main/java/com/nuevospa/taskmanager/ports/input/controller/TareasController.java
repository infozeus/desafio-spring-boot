package com.nuevospa.taskmanager.ports.input.controller;

import com.nuevospa.taskmanager.application.service.impl.TareasServiceImpl;
import com.nuevospa.taskmanager.domain.dto.EstadoTareaDTO;
import com.nuevospa.taskmanager.domain.dto.TareaDTO;
import com.nuevospa.taskmanager.domain.model.EstadosTareas;
import com.nuevospa.taskmanager.domain.model.Tareas;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tareas")
@Tag(name = "Tareas Controller", description = "Operaciones relacionadas con las tareas.")
public class TareasController {

    @Autowired
    private TareasServiceImpl taskService;

    @Operation(
        summary = "Get all tasks",
        description = "Retrieve a list of all tasks.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "List of tasks retrieved successfully",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Tareas.class)
                )
            )
        }
    )
    @GetMapping("/all")
    public ResponseEntity<List<TareaDTO>> getAllTasks() {
        List<TareaDTO> tareaDTOList = taskService.getAllTasks().stream()
                .map(task -> TareaDTO.builder()
                        .id(String.valueOf(task.getId()))
                        .descripcion(task.getDescripcion())
                        .estado(task.getEstado() != null ? 
                            EstadoTareaDTO.builder()
                                .id(String.valueOf(task.getEstado().getId()))
                                .status(task.getEstado().getStatus())
                                .build() : null)
                        .build())   
            .collect(Collectors.toList());
        return ResponseEntity.ok(tareaDTOList);
    }

    @Operation(
        summary = "Get task by ID",
        description = "Retrieve a specific task by its ID.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Task retrieved successfully",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Tareas.class)
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Task not found",
                content = @Content
            )
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<TareaDTO> getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id)
                .map(task -> ResponseEntity.ok(
                        TareaDTO.builder()
                                .id(String.valueOf(task.getId()))
                                .descripcion(task.getDescripcion())
                                .estado(task.getEstado() != null ? 
                                    EstadoTareaDTO.builder()
                                        .id(String.valueOf(task.getEstado().getId()))
                                        .status(task.getEstado().getStatus())
                                        .build() : null)
                                .build()))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Create a new task",
        description = "Create a new task.",
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Task created successfully",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Tareas.class)
                )
            )
        }
    )
    @PostMapping
    public ResponseEntity<TareaDTO> createTask(@RequestBody TareaDTO task) {
        Tareas tareas = new Tareas();
        tareas.setDescripcion(task.getDescripcion());
    
        // Buscar el estado en la base de datos
        EstadosTareas estado = null;
        if (task.getEstado() != null && task.getEstado().getId() != null) {
            Long estadoId = Long.parseLong(task.getEstado().getId());
            Optional<EstadosTareas> estadoOptional = taskService.getEstadoById(estadoId);
            if (estadoOptional.isPresent()) {
                estado = estadoOptional.get();
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(null); 
            }
        }
        tareas.setEstado(estado);
    
        // Crear la tarea
        Tareas createdTask = taskService.createTask(tareas);
    
        // Construir el DTO de respuesta
        TareaDTO createdTaskDTO = TareaDTO.builder()
                .id(String.valueOf(createdTask.getId()))
                .descripcion(createdTask.getDescripcion())
                .estado(createdTask.getEstado() != null ? 
                    EstadoTareaDTO.builder()
                        .id(String.valueOf(createdTask.getEstado().getId()))
                        .status(createdTask.getEstado().getStatus())
                        .build() : null)
                .build();
    
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTaskDTO);
    }
    

    @Operation(
        summary = "Update an existing task",
        description = "Update an existing task.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Task updated successfully",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Tareas.class)
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Task not found",
                content = @Content
            )
        }
    )
    @PutMapping("/{id}")
    public ResponseEntity<TareaDTO> updateTask(@PathVariable Long id, @RequestBody TareaDTO taskDTO) {
        Optional<Tareas> existingTaskOptional = taskService.getTaskById(id);
        if (existingTaskOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
    
        Tareas existingTask = existingTaskOptional.get();
        existingTask.setDescripcion(taskDTO.getDescripcion());
        if (taskDTO.getEstado() != null) {
            EstadosTareas estado = new EstadosTareas();
            estado.setId(Long.parseLong(taskDTO.getEstado().getId()));
            estado.setStatus(taskDTO.getEstado().getStatus());
            existingTask.setEstado(estado);
        }
    
        Tareas updatedTask = taskService.updateTask(existingTask);
    
        TareaDTO updatedTaskDTO = TareaDTO.builder()
                .id(String.valueOf(updatedTask.getId()))
                .descripcion(updatedTask.getDescripcion())
                .estado(updatedTask.getEstado() != null ? 
                    EstadoTareaDTO.builder()
                        .id(String.valueOf(updatedTask.getEstado().getId()))
                        .status(updatedTask.getEstado().getStatus())
                        .build() : null)
                .build();
    
        return ResponseEntity.ok(updatedTaskDTO);
    }

    @Operation(
        summary = "Delete a task",
        description = "Delete a specific task by its ID.",
        responses = {
            @ApiResponse(
                responseCode = "204",
                description = "Task deleted successfully",
                content = @Content
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Task not found",
                content = @Content
            )
        }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
