package dev.rnvo.stravakudosjava.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "strava")
public class StravaProperties {
    private String email;
    private String password;
    private String browser = "edge";
    private boolean headless = true;
}
