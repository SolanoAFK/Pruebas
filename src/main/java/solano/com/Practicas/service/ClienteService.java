package solano.com.Practicas.service;

import solano.com.Practicas.dto.ClienteDTO;
import solano.com.Practicas.entity.Cliente;
import java.util.List;
import java.util.Optional;

public interface ClienteService {
    Cliente crearCliente(Cliente cliente);
    Optional<Cliente> obtenerPorId(Long id);
    List<Cliente> obtenerTodos();
    Cliente actualizar(Long id, Cliente cliente);
    void eliminar(Long id);
    ClienteDTO convertToDTO(Cliente cliente);
}
