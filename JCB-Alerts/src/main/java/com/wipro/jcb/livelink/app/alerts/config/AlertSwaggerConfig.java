package com.wipro.jcb.livelink.app.alerts.config;

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
public class AlertSwaggerConfig {

    @Bean
    public OpenAPI openAPIConfig() {
        return new OpenAPI()
                .info(new Info()
                        .title("JCB-Alerts API")
                        .description("""
                                API documentation for JCB-Alerts microservice.

                                **JCB Alerts: Powering Construction and Beyond**
                                JCB Alerts for synonymous with power, innovation, and durability. For over seven decades, they have been at the forefront of the construction and agricultural industries, providing cutting-edge equipment that empowers businesses worldwide.
                                """)
                        .version("1.0")
                        .contact(new Contact()
                                .name("Team-NextGen")
                                .url("https://www.jcblivelink.in")
                                .email("Team-NextGen@wipro.com")));
    }
}