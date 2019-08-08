package com.biscoito.voucher.config.springfox;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "swagger")
public class SwaggerCustomProperties {
    private String basePath;
}
