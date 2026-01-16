package solano.com.Practicas.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import solano.com.Practicas.dto.ProyectoDTO;
import solano.com.Practicas.entity.Proyecto;
import solano.com.Practicas.response.ApiResponse;
import solano.com.Practicas.service.ProyectoService;
import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/proyectos")
@RequiredArgsConstructor
public class ProyectoController {

    private final ProyectoService proyectoService;

    /**
     * GET /api/proyectos - Obtener todos los proyectos
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProyectoDTO>>> obtenerTodos() {
        List<ProyectoDTO> proyectos = proyectoService.obtenerTodos()
                .stream()
                .map(proyectoService::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Proyectos obtenidos", proyectos));
    }

    /**
     * GET /api/proyectos/{id} - Obtener proyecto por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProyectoDTO>> obtenerPorId(@PathVariable Long id) {
        Proyecto proyecto = proyectoService.obtenerPorId(id)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));
        return ResponseEntity.ok(ApiResponse.success("Proyecto obtenido", proyectoService.convertToDTO(proyecto)));
    }

    /**
     * POST /api/proyectos - Crear nuevo proyecto
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ProyectoDTO>> crear(@Valid @RequestBody Proyecto proyecto) {
        Proyecto nuevoProyecto = proyectoService.crearProyecto(proyecto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Proyecto creado exitosamente", proyectoService.convertToDTO(nuevoProyecto)));
    }

    /**
     * PUT /api/proyectos/{id} - Actualizar proyecto
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProyectoDTO>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody Proyecto proyecto) {
        Proyecto actualizado = proyectoService.actualizar(id, proyecto);
        return ResponseEntity.ok(ApiResponse.success("Proyecto actualizado", proyectoService.convertToDTO(actualizado)));
    }

    /**
     * DELETE /api/proyectos/{id} - Eliminar proyecto
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> eliminar(@PathVariable Long id) {
        proyectoService.eliminar(id);
        return ResponseEntity.ok(ApiResponse.success("Proyecto eliminado exitosamente"));
    }
}
