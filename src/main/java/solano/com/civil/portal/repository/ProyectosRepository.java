package solano.com.civil.portal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import solano.com.civil.portal.entity.Proyectos;

import java.util.List;

@Repository
public interface ProyectosRepository extends JpaRepository<Proyectos, Long> {
    List<Proyectos> findByCliente_Id(Long clienteId);
    List<Proyectos> findByCreatedBy_Id(Long usuarioId);
}
