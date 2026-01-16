package solano.com.Practicas.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import solano.com.Practicas.dto.UsuarioDTO;
import solano.com.Practicas.entity.Usuario;
import solano.com.Practicas.response.ApiResponse;
import solano.com.Practicas.service.UsuarioService;
import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    /**
     * GET /api/usuarios - Obtener todos los usuarios
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<UsuarioDTO>>> obtenerTodos() {
        List<UsuarioDTO> usuarios = usuarioService.obtenerTodos()
                .stream()
                .map(usuarioService::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Usuarios obtenidos", usuarios));
    }

    /**
     * GET /api/usuarios/{id} - Obtener usuario por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UsuarioDTO>> obtenerPorId(@PathVariable Long id) {
        Usuario usuario = usuarioService.obtenerPorId(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return ResponseEntity.ok(ApiResponse.success("Usuario obtenido", usuarioService.convertToDTO(usuario)));
    }

    /**
     * GET /api/usuarios/username/{username} - Obtener usuario por username
     */
    @GetMapping("/username/{username}")
    public ResponseEntity<ApiResponse<UsuarioDTO>> obtenerPorUsername(@PathVariable String username) {
        Usuario usuario = usuarioService.obtenerPorUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return ResponseEntity.ok(ApiResponse.success("Usuario obtenido", usuarioService.convertToDTO(usuario)));
    }

    /**
     * POST /api/usuarios - Crear nuevo usuario
     */
    @PostMapping
    public ResponseEntity<ApiResponse<UsuarioDTO>> crear(@Valid @RequestBody Usuario usuario) {
        Usuario nuevoUsuario = usuarioService.crearUsuario(usuario);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Usuario creado exitosamente", usuarioService.convertToDTO(nuevoUsuario)));
    }

    /**
     * PUT /api/usuarios/{id} - Actualizar usuario
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UsuarioDTO>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody Usuario usuario) {
        Usuario actualizado = usuarioService.actualizar(id, usuario);
        return ResponseEntity.ok(ApiResponse.success("Usuario actualizado", usuarioService.convertToDTO(actualizado)));
    }

    /**
     * DELETE /api/usuarios/{id} - Eliminar usuario
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return ResponseEntity.ok(ApiResponse.success("Usuario eliminado exitosamente"));
    }
}
