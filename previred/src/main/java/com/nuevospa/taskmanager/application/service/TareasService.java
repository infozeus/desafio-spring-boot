package com.nuevospa.taskmanager.application.service;

import java.util.List;
import java.util.Optional;

import com.nuevospa.taskmanager.domain.model.EstadosTareas;
import com.nuevospa.taskmanager.domain.model.Tareas;

public interface TareasService {

    List<Tareas> getAllTasks();
    
    Optional<Tareas> getTaskById(Long id);
    
    Tareas createTask(Tareas task);
    
    Tareas updateTask(Tareas task);
    
    void deleteTask(Long id);

    Optional<EstadosTareas> getEstadoById(Long id);


}
