package solano.com.civil.portal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import solano.com.civil.portal.entity.ProyectoMiembros;
import solano.com.civil.portal.entity.ProyectoMiembrosId;

import java.util.List;

@Repository
public interface ProyectoMiembrosRepository extends JpaRepository<ProyectoMiembros, ProyectoMiembrosId> {
    List<ProyectoMiembros> findByProyecto_Id(Long proyectoId);
    List<ProyectoMiembros> findByUsuario_Id(Long usuarioId);
}
