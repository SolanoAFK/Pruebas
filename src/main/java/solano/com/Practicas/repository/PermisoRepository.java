package solano.com.Practicas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import solano.com.Practicas.entity.Permiso;

import java.util.Optional;

public interface PermisoRepository extends JpaRepository<Permiso, Long> {
    Optional<Permiso> findByCodigo(String codigo);
}
