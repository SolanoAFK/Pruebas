package solano.com.civil.portal.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import solano.com.civil.portal.dto.ApiResponse;

/**
 * Controlador para gestión de proyectos civiles
 * Próxima implementación
 */
@RestController
@RequestMapping("/api/proyectos")
@AllArgsConstructor
@Slf4j
public class ProyectosController {
    
    /**
     * Listar todos los proyectos
     * GET /api/proyectos
     */
    @GetMapping
    @PreAuthorize("hasAuthority('PROYECTO_VER')")
    public ResponseEntity<ApiResponse<String>> listarProyectos() {
        return ResponseEntity.ok(ApiResponse.success("Función en desarrollo"));
    }
    
    /**
     * Obtener proyecto por ID
     * GET /api/proyectos/{id}
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PROYECTO_VER')")
    public ResponseEntity<ApiResponse<String>> obtenerProyecto(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Función en desarrollo"));
    }
    
    /**
     * Crear nuevo proyecto
     * POST /api/proyectos
     */
    @PostMapping
    @PreAuthorize("hasAuthority('PROYECTO_CREAR')")
    public ResponseEntity<ApiResponse<String>> crearProyecto() {
        return ResponseEntity.status(201).body(ApiResponse.success("Función en desarrollo"));
    }
}
