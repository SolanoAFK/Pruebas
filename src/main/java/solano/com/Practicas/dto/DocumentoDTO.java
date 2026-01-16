package solano.com.Practicas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentoDTO {
    private Long id;
    private Long proyectoId;
    private String nombre;
    private String tipo;
    private String extension;
    private String rutaArchivo;
    private String version;
    private String descripcion;
    private Long subidoPorId;
    private LocalDateTime uploadedAt;
}
