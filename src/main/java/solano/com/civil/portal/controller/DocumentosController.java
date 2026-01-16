package solano.com.civil.portal.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import solano.com.civil.portal.dto.ApiResponse;

/**
 * Controlador para gestión de documentos técnicos
 * Próxima implementación
 */
@RestController
@RequestMapping("/api/documentos")
@AllArgsConstructor
@Slf4j
public class DocumentosController {
    
    /**
     * Listar documentos
     * GET /api/documentos
     */
    @GetMapping
    @PreAuthorize("hasAuthority('DOC_VER')")
    public ResponseEntity<ApiResponse<String>> listarDocumentos() {
        return ResponseEntity.ok(ApiResponse.success("Función en desarrollo"));
    }
    
    /**
     * Descargar documento
     * GET /api/documentos/{id}/descargar
     */
    @GetMapping("/{id}/descargar")
    @PreAuthorize("hasAuthority('DOC_DESCARGAR')")
    public ResponseEntity<ApiResponse<String>> descargarDocumento(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Función en desarrollo"));
    }
    
    /**
     * Subir documento
     * POST /api/documentos
     */
    @PostMapping
    @PreAuthorize("hasAuthority('DOC_SUBIR')")
    public ResponseEntity<ApiResponse<String>> subirDocumento() {
        return ResponseEntity.status(201).body(ApiResponse.success("Función en desarrollo"));
    }
}
