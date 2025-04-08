package com.nuevospa.taskmanager.application.service.impl;

import com.nuevospa.taskmanager.domain.model.Usuarios;
import com.nuevospa.taskmanager.domain.repository.UsuariosRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static java.util.Collections.emptyList;

@Service
public class UsuarioDetailsServiceImpl implements UserDetailsService {

    private UsuariosRepository usuariosRepository;

    public UsuarioDetailsServiceImpl(UsuariosRepository usuariosRepository) {
        this.usuariosRepository = usuariosRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuarios usuario = usuariosRepository.findByUsername(username);
        if (usuario == null) {
            throw new UsernameNotFoundException(username);
        }
        return new User(usuario.getUsername(), usuario.getPassword(), emptyList());
    }

    public Usuarios findByUsernameAndPassword(String username, String password) {
        return usuariosRepository.findByUsernameAndPassword(username, password);
    }
}
