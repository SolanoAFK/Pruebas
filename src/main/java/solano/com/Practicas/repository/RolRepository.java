package solano.com.Practicas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import solano.com.Practicas.entity.Rol;

import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Long> {
    Optional<Rol> findByNombre(String nombre);
}
