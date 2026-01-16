package solano.com.Practicas.service;

import solano.com.Practicas.dto.DocumentoDTO;
import solano.com.Practicas.entity.Documento;
import java.util.List;
import java.util.Optional;

public interface DocumentoService {
    Documento crearDocumento(Documento documento);
    Optional<Documento> obtenerPorId(Long id);
    List<Documento> obtenerPorProyecto(Long proyectoId);
    Documento actualizar(Long id, Documento documento);
    void eliminar(Long id);
    DocumentoDTO convertToDTO(Documento documento);
}
