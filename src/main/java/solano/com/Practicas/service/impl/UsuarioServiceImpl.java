package solano.com.Practicas.service.impl;

import solano.com.Practicas.dto.UsuarioDTO;
import solano.com.Practicas.dto.RolDTO;
import solano.com.Practicas.entity.Usuario;
import solano.com.Practicas.exception.ResourceNotFoundException;
import solano.com.Practicas.repository.UsuarioRepository;
import solano.com.Practicas.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Usuario crearUsuario(Usuario usuario) {
        // Encriptar contraseña
        usuario.setPasswordHash(passwordEncoder.encode(usuario.getPasswordHash()));
        usuario.setEstado(1); // Activo por defecto
        return usuarioRepository.save(usuario);
    }

    @Override
    public Optional<Usuario> obtenerPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public Optional<Usuario> obtenerPorUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    @Override
    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario actualizar(Long id, Usuario usuarioActualizado) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));

        if (usuarioActualizado.getNombres() != null) {
            usuario.setNombres(usuarioActualizado.getNombres());
        }
        if (usuarioActualizado.getApellidos() != null) {
            usuario.setApellidos(usuarioActualizado.getApellidos());
        }
        if (usuarioActualizado.getEmail() != null) {
            usuario.setEmail(usuarioActualizado.getEmail());
        }
        if (usuarioActualizado.getEstado() != null) {
            usuario.setEstado(usuarioActualizado.getEstado());
        }

        return usuarioRepository.save(usuario);
    }

    @Override
    public void eliminar(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
        usuarioRepository.delete(usuario);
    }

    @Override
    public UsuarioDTO convertToDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setUsername(usuario.getUsername());
        dto.setNombres(usuario.getNombres());
        dto.setApellidos(usuario.getApellidos());
        dto.setEmail(usuario.getEmail());
        dto.setEstado(usuario.getEstado());
        
        // Convertir roles
        if (usuario.getRoles() != null) {
            dto.setRoles(usuario.getRoles().stream()
                    .map(rol -> new RolDTO(rol.getId(), rol.getNombre(), rol.getDescripcion()))
                    .collect(Collectors.toSet()));
        }
        
        return dto;
    }
}
