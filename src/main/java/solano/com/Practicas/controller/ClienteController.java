package solano.com.Practicas.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import solano.com.Practicas.dto.ClienteDTO;
import solano.com.Practicas.entity.Cliente;
import solano.com.Practicas.response.ApiResponse;
import solano.com.Practicas.service.ClienteService;
import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    /**
     * GET /api/clientes - Obtener todos los clientes
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ClienteDTO>>> obtenerTodos() {
        List<ClienteDTO> clientes = clienteService.obtenerTodos()
                .stream()
                .map(clienteService::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Clientes obtenidos", clientes));
    }

    /**
     * GET /api/clientes/{id} - Obtener cliente por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClienteDTO>> obtenerPorId(@PathVariable Long id) {
        Cliente cliente = clienteService.obtenerPorId(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        return ResponseEntity.ok(ApiResponse.success("Cliente obtenido", clienteService.convertToDTO(cliente)));
    }

    /**
     * POST /api/clientes - Crear nuevo cliente
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ClienteDTO>> crear(@Valid @RequestBody Cliente cliente) {
        Cliente nuevoCliente = clienteService.crearCliente(cliente);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Cliente creado exitosamente", clienteService.convertToDTO(nuevoCliente)));
    }

    /**
     * PUT /api/clientes/{id} - Actualizar cliente
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ClienteDTO>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody Cliente cliente) {
        Cliente actualizado = clienteService.actualizar(id, cliente);
        return ResponseEntity.ok(ApiResponse.success("Cliente actualizado", clienteService.convertToDTO(actualizado)));
    }

    /**
     * DELETE /api/clientes/{id} - Eliminar cliente
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> eliminar(@PathVariable Long id) {
        clienteService.eliminar(id);
        return ResponseEntity.ok(ApiResponse.success("Cliente eliminado exitosamente"));
    }
}
