package solano.com.Practicas.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import solano.com.Practicas.entity.Usuario;
import solano.com.Practicas.repository.UsuarioRepository;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario u = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        if (u.getEstado() != null && u.getEstado() == 0) {
            throw new UsernameNotFoundException("Usuario inactivo: " + username);
        }

        Set<SimpleGrantedAuthority> auths = new HashSet<>();

        // ROLES como ROLE_ADMIN, ROLE_INGENIERO, etc.
        u.getRoles().forEach(rol -> {
            auths.add(new SimpleGrantedAuthority("ROLE_" + rol.getNombre()));

            // PERMISOS como PROYECTO_VER, DOC_SUBIR, etc.
            rol.getPermisos().forEach(p -> auths.add(new SimpleGrantedAuthority(p.getCodigo())));
        });

        return new org.springframework.security.core.userdetails.User(
                u.getUsername(),
                u.getPasswordHash(),
                auths);
    }
}
