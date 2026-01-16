package solano.com.Practicas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProyectoDTO {
    private Long id;
    private String codigo;
    private String nombre;
    private String descripcion;
    private String ubicacion;
    private Long clienteId;
    private String estado;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Long creadoPorId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
