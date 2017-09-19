package pl.sdacademy.infrastructure.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import static java.lang.Boolean.FALSE;

@Data
@Component
@RefreshScope
@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {
    private Boolean debugEnabled = FALSE;
    private String imagesStoragePath;
}
