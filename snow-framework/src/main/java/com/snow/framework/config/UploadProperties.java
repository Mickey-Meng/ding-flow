package com.snow.framework.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "file.upload")
public class UploadProperties {
    private String path;
    private String viewPath;
    private List<String> allowTypes;
}
