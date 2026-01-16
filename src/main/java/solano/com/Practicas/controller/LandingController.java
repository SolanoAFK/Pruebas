package solano.com.Practicas.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import solano.com.Practicas.response.ApiResponse;
import solano.com.Practicas.view.LandingInfo;

import java.util.Arrays;

@RestController
public class LandingController {

    /**
     * GET / - Landing pública
     */
    @GetMapping("/")
    public ResponseEntity<ApiResponse<LandingInfo>> landing() {
        LandingInfo info = new LandingInfo();
        info.setEmpresa("Civil Portal");
        info.setDescripcion("Sistema integrado para gestión de proyectos civiles, documentos y recursos");
        info.setServicios(Arrays.asList(
                "Gestión de proyectos",
                "Administración de documentos",
                "Control de usuarios y roles",
                "Seguimiento de actividades",
                "Gestión de clientes"
        ));
        info.setContacto("Contacto Empresa");
        info.setEmail("info@civilportal.com");
        info.setTelefono("+1-234-567-890");

        return ResponseEntity.ok(ApiResponse.success("Bienvenido a Civil Portal", info));
    }

    /**
     * GET /health - Health check
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> health() {
        return ResponseEntity.ok(ApiResponse.success("API Status", "Servidor activo y funcionando correctamente"));
    }
}
