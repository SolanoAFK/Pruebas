package solano.com.civil.portal.security;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import solano.com.civil.portal.entity.Usuario;

public class CustomUserDetails implements UserDetails {
    
    private final Usuario usuario;
    
    public CustomUserDetails(Usuario usuario) {
        this.usuario = usuario;
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return usuario.getRoles().stream()
                .flatMap(rol -> rol.getPermisos().stream()
                    .map(permiso -> new SimpleGrantedAuthority(permiso.getCodigo()))
                )
                .collect(Collectors.toList());
    }
    
    @Override
    public String getPassword() {
        return usuario.getPasswordHash();
    }
    
    @Override
    public String getUsername() {
        return usuario.getUsername();
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    @Override
    public boolean isEnabled() {
        return usuario.getEstado() == 1;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }
}
