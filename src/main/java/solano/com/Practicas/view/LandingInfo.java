package solano.com.Practicas.view;

import lombok.Data;
import java.util.List;

@Data
public class LandingInfo {
    private String empresa;
    private String descripcion;
    private List<String> servicios;
    private String contacto;
    private String email;
    private String telefono;
}
