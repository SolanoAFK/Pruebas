package solano.com.civil.portal.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "proyecto_miembros")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProyectoMiembros {
    
    @EmbeddedId
    private ProyectoMiembrosId id;
    
    @Column(length = 50)
    private String rolEnProyecto = "MIEMBRO";
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("proyectoId")
    @JoinColumn(name = "proyecto_id")
    private Proyectos proyecto;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("usuarioId")
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
