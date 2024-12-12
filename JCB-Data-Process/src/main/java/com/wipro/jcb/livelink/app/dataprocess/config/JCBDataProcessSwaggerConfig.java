package com.wipro.jcb.livelink.app.dataprocess.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

 /*
 Author: Rituraj Azad
 * User: RI20474447
 * Date:19-09-2024
 *
 */

@Configuration
public class JCBDataProcessSwaggerConfig {

    @Bean
    public OpenAPI openAPIConfig() {
        return new OpenAPI()
                .info(new Info()
                        .title("JCB-Data-Process API")
                        .description("""
                                API documentation for JCB-Data-Process microservice.

                                **JCB-Data-Process: Powering Construction and Beyond**
                                Provides API documentation for the JCB Data Process microservice. This service handles data processing tasks within the JCB Livelink ecosystem.""")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Team-NextGen")
                                .url("https://www.jcblivelink.in")
                                .email("Team-NextGen@wipro.com")));
    }
}