package solano.com.civil.portal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import solano.com.civil.portal.entity.Documentos;

import java.util.List;

@Repository
public interface DocumentosRepository extends JpaRepository<Documentos, Long> {
    List<Documentos> findByProyecto_Id(Long proyectoId);
    List<Documentos> findByUploadedBy_Id(Long usuarioId);
}
