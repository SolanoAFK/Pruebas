package solano.com.Practicas.service;

import solano.com.Practicas.dto.UsuarioDTO;
import solano.com.Practicas.entity.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    Usuario crearUsuario(Usuario usuario);
    Optional<Usuario> obtenerPorId(Long id);
    Optional<Usuario> obtenerPorUsername(String username);
    List<Usuario> obtenerTodos();
    Usuario actualizar(Long id, Usuario usuario);
    void eliminar(Long id);
    UsuarioDTO convertToDTO(Usuario usuario);
}
