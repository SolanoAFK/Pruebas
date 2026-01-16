package solano.com.Practicas.service.impl;

import solano.com.Practicas.dto.ClienteDTO;
import solano.com.Practicas.entity.Cliente;
import solano.com.Practicas.exception.ResourceNotFoundException;
import solano.com.Practicas.repository.ClienteRepository;
import solano.com.Practicas.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public Cliente crearCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Override
    public Optional<Cliente> obtenerPorId(Long id) {
        return clienteRepository.findById(id);
    }

    @Override
    public List<Cliente> obtenerTodos() {
        return clienteRepository.findAll();
    }

    @Override
    public Cliente actualizar(Long id, Cliente clienteActualizado) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + id));

        if (clienteActualizado.getNombre() != null) {
            cliente.setNombre(clienteActualizado.getNombre());
        }
        if (clienteActualizado.getDocumento() != null) {
            cliente.setDocumento(clienteActualizado.getDocumento());
        }
        if (clienteActualizado.getTelefono() != null) {
            cliente.setTelefono(clienteActualizado.getTelefono());
        }
        if (clienteActualizado.getEmail() != null) {
            cliente.setEmail(clienteActualizado.getEmail());
        }
        if (clienteActualizado.getDireccion() != null) {
            cliente.setDireccion(clienteActualizado.getDireccion());
        }

        return clienteRepository.save(cliente);
    }

    @Override
    public void eliminar(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + id));
        clienteRepository.delete(cliente);
    }

    @Override
    public ClienteDTO convertToDTO(Cliente cliente) {
        if (cliente == null) {
            return null;
        }
        
        return new ClienteDTO(
                cliente.getId(),
                cliente.getNombre(),
                cliente.getDocumento(),
                cliente.getTelefono(),
                cliente.getEmail(),
                cliente.getDireccion()
        );
    }
}
