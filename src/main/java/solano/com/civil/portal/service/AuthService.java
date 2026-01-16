package solano.com.civil.portal.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import solano.com.civil.portal.dto.LoginRequest;
import solano.com.civil.portal.dto.LoginResponse;
import solano.com.civil.portal.dto.RegisterRequest;
import solano.com.civil.portal.entity.Rol;
import solano.com.civil.portal.entity.Usuario;
import solano.com.civil.portal.repository.RolRepository;
import solano.com.civil.portal.repository.UsuarioRepository;
import solano.com.civil.portal.security.CustomUserDetails;
import solano.com.civil.portal.security.JwtUtil;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService {
    
    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    
    /**
     * Autentica un usuario y genera un JWT
     */
    public LoginResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword()
                )
            );
            
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Usuario usuario = userDetails.getUsuario();
            
            String token = jwtUtil.generateToken(userDetails);
            
            log.info("Usuario autenticado: {}", usuario.getUsername());
            
            return LoginResponse.builder()
                .token(token)
                .username(usuario.getUsername())
                .usuarioId(usuario.getId())
                .exito(true)
                .mensaje("Autenticación exitosa")
                .build();
            
        } catch (AuthenticationException e) {
            log.error("Error de autenticación: {}", e.getMessage());
            return LoginResponse.builder()
                .exito(false)
                .mensaje("Credenciales inválidas")
                .build();
        }
    }
    
    /**
     * Registra un nuevo usuario
     */
    public LoginResponse register(RegisterRequest request) {
        try {
            // Validar que el usuario no exista
            if (usuarioRepository.existsByUsername(request.getUsername())) {
                return LoginResponse.builder()
                    .exito(false)
                    .mensaje("El usuario ya existe")
                    .build();
            }
            
            // Crear nuevo usuario
            Usuario usuario = new Usuario();
            usuario.setUsername(request.getUsername());
            usuario.setPasswordHash(passwordEncoder.encode(request.getPassword()));
            usuario.setNombres(request.getNombres());
            usuario.setApellidos(request.getApellidos());
            usuario.setEmail(request.getEmail());
            usuario.setEstado(1); // Activo
            usuario.setCreatedAt(LocalDateTime.now());
            
            // Asignar rol por defecto (VISOR)
            Rol rolVisor = rolRepository.findByNombre("VISOR")
                .orElseThrow(() -> new RuntimeException("Rol VISOR no encontrado"));
            
            Set<Rol> roles = new HashSet<>();
            roles.add(rolVisor);
            usuario.setRoles(roles);
            
            usuario = usuarioRepository.save(usuario);
            
            log.info("Usuario registrado: {}", usuario.getUsername());
            
            return LoginResponse.builder()
                .exito(true)
                .usuarioId(usuario.getId())
                .username(usuario.getUsername())
                .mensaje("Usuario registrado exitosamente. Por favor inicie sesión.")
                .build();
            
        } catch (Exception e) {
            log.error("Error al registrar usuario: {}", e.getMessage());
            return LoginResponse.builder()
                .exito(false)
                .mensaje("Error al registrar usuario: " + e.getMessage())
                .build();
        }
    }
}
