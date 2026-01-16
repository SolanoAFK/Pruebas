package solano.com.Practicas.service.impl;

import solano.com.Practicas.dto.ProyectoDTO;
import solano.com.Practicas.entity.Proyecto;
import solano.com.Practicas.exception.ResourceNotFoundException;
import solano.com.Practicas.repository.ProyectoRepository;
import solano.com.Practicas.service.ProyectoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProyectoServiceImpl implements ProyectoService {

    @Autowired
    private ProyectoRepository proyectoRepository;

    @Override
    public Proyecto crearProyecto(Proyecto proyecto) {
        return proyectoRepository.save(proyecto);
    }

    @Override
    public Optional<Proyecto> obtenerPorId(Long id) {
        return proyectoRepository.findById(id);
    }

    @Override
    public Optional<Proyecto> obtenerPorCodigo(String codigo) {
        return proyectoRepository.findByCodigo(codigo);
    }

    @Override
    public List<Proyecto> obtenerTodos() {
        return proyectoRepository.findAll();
    }

    @Override
    public Proyecto actualizar(Long id, Proyecto proyectoActualizado) {
        Proyecto proyecto = proyectoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado con ID: " + id));

        if (proyectoActualizado.getNombre() != null) {
            proyecto.setNombre(proyectoActualizado.getNombre());
        }
        if (proyectoActualizado.getDescripcion() != null) {
            proyecto.setDescripcion(proyectoActualizado.getDescripcion());
        }
        if (proyectoActualizado.getUbicacion() != null) {
            proyecto.setUbicacion(proyectoActualizado.getUbicacion());
        }
        if (proyectoActualizado.getEstado() != null) {
            proyecto.setEstado(proyectoActualizado.getEstado());
        }
        if (proyectoActualizado.getFechaInicio() != null) {
            proyecto.setFechaInicio(proyectoActualizado.getFechaInicio());
        }
        if (proyectoActualizado.getFechaFin() != null) {
            proyecto.setFechaFin(proyectoActualizado.getFechaFin());
        }
        if (proyectoActualizado.getCliente() != null) {
            proyecto.setCliente(proyectoActualizado.getCliente());
        }

        return proyectoRepository.save(proyecto);
    }

    @Override
    public void eliminar(Long id) {
        Proyecto proyecto = proyectoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado con ID: " + id));
        proyectoRepository.delete(proyecto);
    }

    @Override
    public ProyectoDTO convertToDTO(Proyecto proyecto) {
        if (proyecto == null) {
            return null;
        }
        
        ProyectoDTO dto = new ProyectoDTO();
        dto.setId(proyecto.getId());
        dto.setCodigo(proyecto.getCodigo());
        dto.setNombre(proyecto.getNombre());
        dto.setDescripcion(proyecto.getDescripcion());
        dto.setUbicacion(proyecto.getUbicacion());
        dto.setEstado(proyecto.getEstado());
        dto.setFechaInicio(proyecto.getFechaInicio());
        dto.setFechaFin(proyecto.getFechaFin());
        if (proyecto.getCliente() != null) {
            dto.setClienteId(proyecto.getCliente().getId());
        }
        if (proyecto.getCreadoPor() != null) {
            dto.setCreadoPorId(proyecto.getCreadoPor().getId());
        }
        dto.setCreatedAt(proyecto.getCreatedAt());
        dto.setUpdatedAt(proyecto.getUpdatedAt());
        
        return dto;
    }
}
