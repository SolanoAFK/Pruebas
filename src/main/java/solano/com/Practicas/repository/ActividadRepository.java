package solano.com.Practicas.repository;

import solano.com.Practicas.entity.Actividad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ActividadRepository extends JpaRepository<Actividad, Long> {
    List<Actividad> findByUsuarioId(Long usuarioId);
    List<Actividad> findByProyectoId(Long proyectoId);
}
