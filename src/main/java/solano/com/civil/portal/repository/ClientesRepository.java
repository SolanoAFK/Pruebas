package solano.com.civil.portal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import solano.com.civil.portal.entity.Clientes;

@Repository
public interface ClientesRepository extends JpaRepository<Clientes, Long> {
}
