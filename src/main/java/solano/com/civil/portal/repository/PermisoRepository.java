package solano.com.civil.portal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import solano.com.civil.portal.entity.Permiso;

import java.util.Optional;

@Repository
public interface PermisoRepository extends JpaRepository<Permiso, Long> {
    Optional<Permiso> findByCodigo(String codigo);
}
