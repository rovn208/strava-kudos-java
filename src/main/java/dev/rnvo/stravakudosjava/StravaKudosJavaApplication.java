package dev.rnvo.stravakudosjava;

import dev.rnvo.stravakudosjava.config.StravaProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystems;
import java.util.Collections;

@SpringBootApplication
@ConfigurationPropertiesScan(basePackageClasses = {StravaProperties.class})
@EnableConfigurationProperties
public class StravaKudosJavaApplication {

    public static void main(String[] args) throws IOException {
        // VM options: -agentlib:native-image-agent=config-output-dir=src/main/resources/META-INF/native-image
        if (null != System.getProperty("org.graalvm.nativeimage.imagecode") || "runtime".equals(System.getProperty("org.graalvm.nativeimage.imagecode"))) {
            FileSystems.newFileSystem(URI.create("resource:/"), Collections.emptyMap());
        }
        SpringApplication.run(StravaKudosJavaApplication.class, args);
    }

}
