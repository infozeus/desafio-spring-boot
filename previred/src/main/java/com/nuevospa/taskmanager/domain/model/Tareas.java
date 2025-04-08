package com.nuevospa.taskmanager.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
    
    @Entity
    @Table(name = "tareas")
    public class Tareas {
    
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String descripcion;
    
        @ManyToOne
        @JoinColumn(name = "status_id", referencedColumnName = "id")
        private EstadosTareas estado;
    
        public Tareas() {
        }
    
        public Tareas(Long id, String descripcion, EstadosTareas estado) {
            this.id = id;
            this.descripcion = descripcion;
            this.estado = estado;
        }
    
        public Long getId() {
            return id;
        }
    
        public void setId(Long id) {
            this.id = id;
        }
    
        public String getDescripcion() {
            return descripcion;
        }
    
        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }
    
        public EstadosTareas getEstado() {
            return estado;
        }
    
        public void setEstado(EstadosTareas estado) {
            this.estado = estado;
        }
    }
    

