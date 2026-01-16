package solano.com.civil.portal.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "documentos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Documentos {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proyecto_id", nullable = false)
    private Proyectos proyecto;
    
    @Column(nullable = false, length = 255)
    private String nombre;
    
    @Column(nullable = false, length = 30)
    private String tipo;
    
    @Column(length = 15)
    private String extension;
    
    @Column(nullable = false, length = 500)
    private String rutaArchivo;
    
    @Column(length = 30)
    private String version = "1.0";
    
    @Column(length = 255)
    private String descripcion;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by")
    private Usuario uploadedBy;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime uploadedAt;
    
    @PrePersist
    protected void onCreate() {
        uploadedAt = LocalDateTime.now();
    }
}
