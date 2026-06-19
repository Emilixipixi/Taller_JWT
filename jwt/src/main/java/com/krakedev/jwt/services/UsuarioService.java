package com.krakedev.jwt.services;

import com.krakedev.jwt.entidades.Usuario;
import com.krakedev.jwt.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario registrar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Usuario login(String username, String password) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Credenciales incorrectas"));
        
        if (!usuario.getPassword().equals(password)) {
            throw new RuntimeException("Credenciales incorrectas");
        }
        
        return usuario;
    }
}