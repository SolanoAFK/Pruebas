package solano.com.Practicas.entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, unique = true, length = 50)
    private String nombre; // ADMIN, INGENIERO, SUPERVISOR, VISOR, etc.

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // N a N: rol_permisos
    @Builder.Default
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "rol_permisos", joinColumns = @JoinColumn(name = "rol_id"), inverseJoinColumns = @JoinColumn(name = "permiso_id"))
    @JsonIgnore
    private Set<Permiso> permisos = new HashSet<>();

    @PrePersist
    protected void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}

