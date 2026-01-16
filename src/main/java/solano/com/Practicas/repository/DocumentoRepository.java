package solano.com.Practicas.repository;

import solano.com.Practicas.entity.Documento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DocumentoRepository extends JpaRepository<Documento, Long> {
    List<Documento> findByProyectoId(Long proyectoId);
}
