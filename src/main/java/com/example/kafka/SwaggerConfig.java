package com.example.kafka;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by HurleyD on 08/05/2017.
 */
@Configuration
@EnableSwagger2
@Slf4j
public class SwaggerConfig extends WebMvcConfigurerAdapter {

    @Autowired
    Environment environment;

    @Bean
    public Docket kafkaControllerApi() {
        log.warn("Swagger UI enabled under /swagger-ui.html. Please don't do this in higher environments.");

        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage(KafkaController.class.getPackage().getName()))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(new ApiInfo("Kafka Controller API", "API Doc for Entitlement Summary Service", "", "", (Contact) null, "", "", Lists.newArrayList()));
    }

}
