package solano.com.Practicas.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;

@Entity
@Table(name = "proyecto_miembros")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProyectoMiembro {

    @EmbeddedId
    private ProyectoMiembroId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("proyectoId")
    @JoinColumn(name = "proyecto_id")
    @JsonIgnore
    private Proyecto proyecto;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("usuarioId")
    @JoinColumn(name = "usuario_id")
    @JsonIgnore
    private Usuario usuario;

    @Column(name = "rol_en_proyecto", length = 50)
    private String rolEnProyecto = "MIEMBRO"; // MIEMBRO, LIDER, SUPERVISOR

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
