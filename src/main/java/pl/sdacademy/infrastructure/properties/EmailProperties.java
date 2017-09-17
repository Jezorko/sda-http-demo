package pl.sdacademy.infrastructure.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Data
@Component
@RefreshScope
@ConfigurationProperties(prefix = "spring.mail")
public class EmailProperties {
    private String host;
    private Integer port;
    private String username;
    private String password;
}
