package com.example.petsitterservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

    @OpenAPIDefinition(
            info = @Info(
                    title = "Pet Boarding Service API",
                    description = "Pet Boarding Service", version = "1.0.0",
                    contact = @Contact(
                            name = "Martynova Svetlana",
                            email = "svetamart95@gmail.com"
                    )
            )
    )
    public class OpenApiConfig {

    }

