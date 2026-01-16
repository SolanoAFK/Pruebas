package solano.com.Practicas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    private Long id;
    private String username;
    private String nombres;
    private String apellidos;
    private String email;
    private Integer estado;
    private Set<RolDTO> roles;
}
