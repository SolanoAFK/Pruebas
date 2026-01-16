package solano.com.Practicas.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonInclude;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private String error;

    // Constructor para respuestas exitosas
    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    // Constructor para respuestas de error
    public ApiResponse(boolean success, String message, String error) {
        this.success = success;
        this.message = message;
        this.error = error;
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<>(true, message, null);
    }

    public static <T> ApiResponse<T> error(String message, String error) {
        return new ApiResponse<>(false, message, error);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, (String) null);
    }
}
