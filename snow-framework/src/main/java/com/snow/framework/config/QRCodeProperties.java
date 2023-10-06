package com.snow.framework.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @ClassName QRCodeProperties
 * @Description TODO
 * @Author Karl
 * @Date 2022/6/21 17:26
 * @Version 1.0
 */

@Data
@Component
@ConfigurationProperties(prefix = "serveraddr")
public class QRCodeProperties {
    private String localhost;
    private String hostAddress;

}
