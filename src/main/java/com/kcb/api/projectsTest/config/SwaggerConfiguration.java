package com.kcb.api.projectsTest.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author martin
 */
@Configuration
public class SwaggerConfiguration {
    
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .info(new Info().title("Project Tasks Api")
                .description("KCB developer Practical Test."));
    }


}
