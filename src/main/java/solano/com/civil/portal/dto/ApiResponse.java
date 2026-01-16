package solano.com.civil.portal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {
    private boolean exito;
    private String mensaje;
    private T datos;
    private Long timestamp;
    
    public ApiResponse(boolean exito, String mensaje, T datos) {
        this.exito = exito;
        this.mensaje = mensaje;
        this.datos = datos;
        this.timestamp = System.currentTimeMillis();
    }
    
    public static <T> ApiResponse<T> success(String mensaje, T datos) {
        return new ApiResponse<>(true, mensaje, datos);
    }
    
    public static <T> ApiResponse<T> success(String mensaje) {
        return new ApiResponse<>(true, mensaje, null);
    }
    
    public static <T> ApiResponse<T> error(String mensaje) {
        return new ApiResponse<>(false, mensaje, null);
    }
    
    public static <T> ApiResponse<T> error(String mensaje, T datos) {
        return new ApiResponse<>(false, mensaje, datos);
    }
}
