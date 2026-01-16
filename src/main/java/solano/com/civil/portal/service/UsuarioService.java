package solano.com.civil.portal.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import solano.com.civil.portal.entity.Usuario;
import solano.com.civil.portal.repository.UsuarioRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UsuarioService {
    
    private final UsuarioRepository usuarioRepository;
    
    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }
    
    public Optional<Usuario> getUsuarioById(Long id) {
        return usuarioRepository.findById(id);
    }
    
    public Optional<Usuario> getUsuarioByUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }
    
    public Optional<Usuario> getUsuarioByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
    
    public Usuario saveUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
    
    public void deleteUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }
    
    public void desactivarUsuario(Long id) {
        usuarioRepository.findById(id).ifPresent(usuario -> {
            usuario.setEstado(0);
            usuarioRepository.save(usuario);
        });
    }
}
