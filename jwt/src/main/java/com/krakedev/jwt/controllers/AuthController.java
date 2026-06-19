package com.krakedev.jwt.controllers;

import com.krakedev.jwt.entidades.Usuario;
import com.krakedev.jwt.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/registrar")
    public ResponseEntity<Usuario> registrar(@RequestBody Usuario usuario) {
        return ResponseEntity.ok(usuarioService.registrar(usuario));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario usuario) {
        try {
            Usuario usuarioAutenticado = usuarioService.login(usuario.getUsername(), usuario.getPassword());
            return ResponseEntity.ok(usuarioAutenticado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
}