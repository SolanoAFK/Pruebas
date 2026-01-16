package solano.com.Practicas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermisoDTO {
    private Long id;
    private String codigo;
    private String descripcion;
}
