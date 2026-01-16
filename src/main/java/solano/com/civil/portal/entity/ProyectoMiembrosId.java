package solano.com.civil.portal.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProyectoMiembrosId implements Serializable {
    private Long proyectoId;
    private Long usuarioId;
}
