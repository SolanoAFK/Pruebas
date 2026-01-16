package solano.com.Practicas.service.impl;

import solano.com.Practicas.dto.DocumentoDTO;
import solano.com.Practicas.entity.Documento;
import solano.com.Practicas.exception.ResourceNotFoundException;
import solano.com.Practicas.repository.DocumentoRepository;
import solano.com.Practicas.service.DocumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DocumentoServiceImpl implements DocumentoService {

    @Autowired
    private DocumentoRepository documentoRepository;

    @Override
    public Documento crearDocumento(Documento documento) {
        return documentoRepository.save(documento);
    }

    @Override
    public Optional<Documento> obtenerPorId(Long id) {
        return documentoRepository.findById(id);
    }

    @Override
    public List<Documento> obtenerPorProyecto(Long proyectoId) {
        return documentoRepository.findByProyectoId(proyectoId);
    }

    @Override
    public Documento actualizar(Long id, Documento documentoActualizado) {
        Documento documento = documentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Documento no encontrado con ID: " + id));

        if (documentoActualizado.getNombre() != null) {
            documento.setNombre(documentoActualizado.getNombre());
        }
        if (documentoActualizado.getDescripcion() != null) {
            documento.setDescripcion(documentoActualizado.getDescripcion());
        }
        if (documentoActualizado.getVersion() != null) {
            documento.setVersion(documentoActualizado.getVersion());
        }

        return documentoRepository.save(documento);
    }

    @Override
    public void eliminar(Long id) {
        Documento documento = documentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Documento no encontrado con ID: " + id));
        documentoRepository.delete(documento);
    }

    @Override
    public DocumentoDTO convertToDTO(Documento documento) {
        if (documento == null) {
            return null;
        }
        
        DocumentoDTO dto = new DocumentoDTO();
        dto.setId(documento.getId());
        if (documento.getProyecto() != null) {
            dto.setProyectoId(documento.getProyecto().getId());
        }
        dto.setNombre(documento.getNombre());
        dto.setTipo(documento.getTipo());
        dto.setExtension(documento.getExtension());
        dto.setRutaArchivo(documento.getRutaArchivo());
        dto.setVersion(documento.getVersion());
        dto.setDescripcion(documento.getDescripcion());
        if (documento.getSubidoPor() != null) {
            dto.setSubidoPorId(documento.getSubidoPor().getId());
        }
        dto.setUploadedAt(documento.getUploadedAt());
        
        return dto;
    }
}
