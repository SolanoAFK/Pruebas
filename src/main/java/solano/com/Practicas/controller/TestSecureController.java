package solano.com.Practicas.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class TestSecureController {

    @GetMapping("/me")
    public Map<String, String> me() {
        return Map.of("message", "Autenticado OK");
    }
}
