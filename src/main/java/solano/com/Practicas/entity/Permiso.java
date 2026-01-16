package solano.com.Practicas.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "permisos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permiso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo", nullable = false, unique = true, length = 80)
    private String codigo; // ej: PROYECTO_CREAR

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
