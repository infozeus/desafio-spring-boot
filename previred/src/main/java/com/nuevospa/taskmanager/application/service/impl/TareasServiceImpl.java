package com.nuevospa.taskmanager.application.service.impl;

import com.nuevospa.taskmanager.application.service.TareasService;
import com.nuevospa.taskmanager.domain.model.EstadosTareas;
import com.nuevospa.taskmanager.domain.model.Tareas;
import com.nuevospa.taskmanager.domain.repository.EstadosTareasRepository;
import com.nuevospa.taskmanager.domain.repository.TareasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TareasServiceImpl implements TareasService {

    @Autowired
    private TareasRepository tareasRepository;

    @Autowired
    private EstadosTareasRepository estadosTareasRepository;

    public List<Tareas> getAllTasks() {
        return tareasRepository.findAll();
    }

    public Optional<Tareas> getTaskById(Long id) {
        return tareasRepository.findById(id);
    }

    public Tareas createTask(Tareas task) {
        return tareasRepository.save(task);
    }

    public Tareas updateTask(Tareas task) {
        return tareasRepository.save(task);
    }

    public void deleteTask(Long id) {
        tareasRepository.deleteById(id);
    }

    public Optional<EstadosTareas> getEstadoById(Long id) {
    return estadosTareasRepository.findById(id);
}

}
