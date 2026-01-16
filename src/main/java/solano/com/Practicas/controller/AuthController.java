package solano.com.Practicas.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import solano.com.Practicas.dto.LoginRequest;
import solano.com.Practicas.dto.LoginResponse;
import solano.com.Practicas.exception.UnauthorizedException;
import solano.com.Practicas.response.ApiResponse;
import solano.com.Practicas.security.JwtService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    /**
     * Endpoint de Login: POST /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest req) {
        try {
            // Autenticar credenciales
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.username(), req.password()));

            UserDetails user = (UserDetails) auth.getPrincipal();
            String token = jwtService.generateToken(user);

            LoginResponse response = new LoginResponse("Bearer", token);
            return ResponseEntity.ok(ApiResponse.success("Login exitoso", response));

        } catch (BadCredentialsException | UsernameNotFoundException ex) {
            throw new UnauthorizedException("Usuario o contraseña inválidos");
        }
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/ping")
    public ResponseEntity<ApiResponse<String>> ping() {
        return ResponseEntity.ok(ApiResponse.success("Pong", "API activa"));
    }
}
