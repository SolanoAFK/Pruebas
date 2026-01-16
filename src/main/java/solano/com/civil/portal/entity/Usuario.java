package solano.com.civil.portal.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, length = 60)
    private String username;
    
    @Column(nullable = false)
    private String passwordHash;
    
    @Column(length = 120)
    private String nombres;
    
    @Column(length = 120)
    private String apellidos;
    
    @Column(length = 120)
    private String email;
    
    @Column(nullable = false)
    private Integer estado;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(insertable = false)
    private LocalDateTime updatedAt;
    
    @ManyToMany(fetch = jakarta.persistence.FetchType.EAGER)
    @JoinTable(
        name = "usuario_roles",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    private Set<Rol> roles;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
