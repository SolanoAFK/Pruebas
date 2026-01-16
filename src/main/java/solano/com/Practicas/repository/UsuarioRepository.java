package solano.com.Practicas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import solano.com.Practicas.entity.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);

    boolean existsByUsername(String username);
}
