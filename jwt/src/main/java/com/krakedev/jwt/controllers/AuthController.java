package com.krakedev.jwt.controllers;

import com.krakedev.jwt.entidades.Usuario;
import com.krakedev.jwt.services.UsuarioService;
import com.krakedev.jwt.utils.JwtUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

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
            String token = JwtUtil.generarToken(usuarioAutenticado.getUsername(), usuarioAutenticado.getRol());
            
            Map<String, String> respuesta = new HashMap<>();
            respuesta.put("token", token);
            
            return ResponseEntity.ok(respuesta);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    @GetMapping("/perfil")
    public ResponseEntity<?> verPerfil(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Falta el token de autenticación");
        }

        try {
            String token = authHeader.substring(7);
            Algorithm algorithm = Algorithm.HMAC256("ClaveSecretaUltraSeguraDelRefugioParaMascotas2026");
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);

            String username = jwt.getSubject();
            String rol = jwt.getClaim("rol").asString();

            Map<String, String> data = new HashMap<>();
            data.put("mensaje", "Bienvenido " + username + " al panel de control");
            data.put("rol", rol);

            return ResponseEntity.ok(data);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Token inválido o expirado");
        }
    }
}