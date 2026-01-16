package solano.com.Practicas.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import solano.com.Practicas.dto.DocumentoDTO;
import solano.com.Practicas.entity.Documento;
import solano.com.Practicas.response.ApiResponse;
import solano.com.Practicas.service.DocumentoService;
import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/documentos")
@RequiredArgsConstructor
public class DocumentoController {

    private final DocumentoService documentoService;

    /**
     * GET /api/documentos - Obtener todos los documentos
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<DocumentoDTO>>> obtenerTodos() {
        // Nota: Para producción, podrías paginar esto
        List<DocumentoDTO> documentos = new java.util.ArrayList<>();
        // Aquí iría la lógica de búsqueda general si es necesaria
        return ResponseEntity.ok(ApiResponse.success("Documentos obtenidos", documentos));
    }

    /**
     * GET /api/documentos/{id} - Obtener documento por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DocumentoDTO>> obtenerPorId(@PathVariable Long id) {
        Documento documento = documentoService.obtenerPorId(id)
                .orElseThrow(() -> new RuntimeException("Documento no encontrado"));
        return ResponseEntity.ok(ApiResponse.success("Documento obtenido", documentoService.convertToDTO(documento)));
    }

    /**
     * GET /api/documentos/proyecto/{proyectoId} - Obtener documentos por proyecto
     */
    @GetMapping("/proyecto/{proyectoId}")
    public ResponseEntity<ApiResponse<List<DocumentoDTO>>> obtenerPorProyecto(@PathVariable Long proyectoId) {
        List<DocumentoDTO> documentos = documentoService.obtenerPorProyecto(proyectoId)
                .stream()
                .map(documentoService::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Documentos del proyecto obtenidos", documentos));
    }

    /**
     * POST /api/documentos - Crear nuevo documento
     */
    @PostMapping
    public ResponseEntity<ApiResponse<DocumentoDTO>> crear(@Valid @RequestBody Documento documento) {
        Documento nuevoDocumento = documentoService.crearDocumento(documento);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Documento creado exitosamente", documentoService.convertToDTO(nuevoDocumento)));
    }

    /**
     * PUT /api/documentos/{id} - Actualizar documento
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DocumentoDTO>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody Documento documento) {
        Documento actualizado = documentoService.actualizar(id, documento);
        return ResponseEntity.ok(ApiResponse.success("Documento actualizado", documentoService.convertToDTO(actualizado)));
    }

    /**
     * DELETE /api/documentos/{id} - Eliminar documento
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> eliminar(@PathVariable Long id) {
        documentoService.eliminar(id);
        return ResponseEntity.ok(ApiResponse.success("Documento eliminado exitosamente"));
    }
}
