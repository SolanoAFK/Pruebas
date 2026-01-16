package solano.com.Practicas.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Entity
@Table(name = "usuario_roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRol implements Serializable {

    @EmbeddedId
    private UsuarioRolId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("usuarioId")
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("rolId")
    @JoinColumn(name = "rol_id")
    private Rol rol;
}
