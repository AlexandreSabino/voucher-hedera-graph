package com.biscoito.voucher.config.springfox;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;
import static springfox.documentation.swagger.web.UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS;

@Configuration
@EnableSwagger2
@RequiredArgsConstructor
@EnableConfigurationProperties(SwaggerCustomProperties.class)
public class SpringFoxConfig {

    private final SwaggerCustomProperties swaggerCustomProperties;

    @Bean
    public Docket documentation() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(regex("/api/.*"))
                .build()
                .pathMapping(swaggerCustomProperties.getBasePath())
                .useDefaultResponseMessages(false)
                .apiInfo(apiInfo());
    }

    @Bean
    public UiConfiguration uiConfig() {
        return UiConfigurationBuilder.builder()
                .validatorUrl("validatorUrl")
                .supportedSubmitMethods(DEFAULT_SUBMIT_METHODS)
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Voucher").version("1.0").build();
    }
}
