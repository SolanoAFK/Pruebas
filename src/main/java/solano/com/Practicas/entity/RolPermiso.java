package solano.com.Practicas.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rol_permisos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolPermiso {

    @EmbeddedId
    private RolPermisoId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("rolId")
    @JoinColumn(name = "rol_id")
    private Rol rol;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("permisoId")
    @JoinColumn(name = "permiso_id")
    private Permiso permiso;
}
