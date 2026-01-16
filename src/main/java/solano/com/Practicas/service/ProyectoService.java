package solano.com.Practicas.service;

import solano.com.Practicas.dto.ProyectoDTO;
import solano.com.Practicas.entity.Proyecto;
import java.util.List;
import java.util.Optional;

public interface ProyectoService {
    Proyecto crearProyecto(Proyecto proyecto);
    Optional<Proyecto> obtenerPorId(Long id);
    Optional<Proyecto> obtenerPorCodigo(String codigo);
    List<Proyecto> obtenerTodos();
    Proyecto actualizar(Long id, Proyecto proyecto);
    void eliminar(Long id);
    ProyectoDTO convertToDTO(Proyecto proyecto);
}
