package solano.com.Practicas.repository;

import solano.com.Practicas.entity.ProyectoMiembro;
import solano.com.Practicas.entity.ProyectoMiembroId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProyectoMiembroRepository extends JpaRepository<ProyectoMiembro, ProyectoMiembroId> {
    List<ProyectoMiembro> findByProyectoId(Long proyectoId);
    List<ProyectoMiembro> findByUsuarioId(Long usuarioId);
}
