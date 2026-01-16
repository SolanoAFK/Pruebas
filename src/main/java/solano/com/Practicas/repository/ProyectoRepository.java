package solano.com.Practicas.repository;

import solano.com.Practicas.entity.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {
    Optional<Proyecto> findByCodigo(String codigo);
}
