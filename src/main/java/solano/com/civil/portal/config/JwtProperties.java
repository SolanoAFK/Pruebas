package solano.com.civil.portal.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
public class JwtProperties {
    private String secret = "civilPortalSecretKeyForJWTTokenGenerationAndValidation2025";
    private Long expiration = 86400000L;
}
