package solano.com.civil.portal.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import solano.com.civil.portal.dto.ApiResponse;
import solano.com.civil.portal.dto.LoginRequest;
import solano.com.civil.portal.dto.LoginResponse;
import solano.com.civil.portal.dto.RegisterRequest;
import solano.com.civil.portal.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@Slf4j
public class AuthController {
    
    private final AuthService authService;
    
    /**
     * Endpoint para login
     * POST /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
        log.info("Intento de login para usuario: {}", request.getUsername());
        
        LoginResponse response = authService.login(request);
        
        if (response.isExito()) {
            return ResponseEntity.ok(ApiResponse.success("Autenticación exitosa", response));
        } else {
            return ResponseEntity.status(401).body(ApiResponse.error(response.getMensaje(), response));
        }
    }
    
    /**
     * Endpoint para registrar un nuevo usuario
     * POST /api/auth/register
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<LoginResponse>> register(@RequestBody RegisterRequest request) {
        log.info("Intento de registro para usuario: {}", request.getUsername());
        
        LoginResponse response = authService.register(request);
        
        if (response.isExito()) {
            return ResponseEntity.status(201).body(ApiResponse.success("Usuario registrado exitosamente", response));
        } else {
            return ResponseEntity.status(400).body(ApiResponse.error(response.getMensaje(), response));
        }
    }
    
    /**
     * Endpoint de prueba para verificar que el token es válido
     * GET /api/auth/validate
     */
    @GetMapping("/validate")
    public ResponseEntity<ApiResponse<String>> validateToken() {
        return ResponseEntity.ok(ApiResponse.success("Token válido", "El token JWT es válido"));
    }
}
