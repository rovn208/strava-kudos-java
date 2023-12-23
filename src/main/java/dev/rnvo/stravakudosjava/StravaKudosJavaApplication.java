package dev.rnvo.stravakudosjava;

import dev.rnvo.stravakudosjava.config.StravaProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@ConfigurationPropertiesScan(basePackageClasses = {StravaProperties.class})
@EnableConfigurationProperties
public class StravaKudosJavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(StravaKudosJavaApplication.class, args);
    }

}
