package solano.com.civil.portal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import solano.com.civil.portal.entity.Actividad;

import java.util.List;

@Repository
public interface ActividadRepository extends JpaRepository<Actividad, Long> {
    List<Actividad> findByProyecto_IdOrderByCreatedAtDesc(Long proyectoId);
    List<Actividad> findByUsuario_IdOrderByCreatedAtDesc(Long usuarioId);
}
