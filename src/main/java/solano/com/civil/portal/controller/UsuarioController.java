package solano.com.civil.portal.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import solano.com.civil.portal.dto.ApiResponse;
import solano.com.civil.portal.entity.Usuario;
import solano.com.civil.portal.service.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@AllArgsConstructor
@Slf4j
public class UsuarioController {
    
    private final UsuarioService usuarioService;
    
    /**
     * Obtener todos los usuarios (solo ADMIN)
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<Usuario>>> getAllUsuarios() {
        List<Usuario> usuarios = usuarioService.getAllUsuarios();
        return ResponseEntity.ok(ApiResponse.success("Usuarios obtenidos", usuarios));
    }
    
    /**
     * Obtener usuario por ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USUARIO_VER')")
    public ResponseEntity<ApiResponse<Usuario>> getUsuarioById(@PathVariable Long id) {
        return usuarioService.getUsuarioById(id)
            .map(usuario -> ResponseEntity.ok(ApiResponse.success("Usuario encontrado", usuario)))
            .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Obtener el perfil del usuario autenticado
     */
    @GetMapping("/perfil/me")
    public ResponseEntity<ApiResponse<String>> getMyProfile() {
        return ResponseEntity.ok(ApiResponse.success("Perfil obtenido", "Usuario autenticado correctamente"));
    }
}
