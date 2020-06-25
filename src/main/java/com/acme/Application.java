package com.acme;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@OpenAPIDefinition(
        info = @Info(
                title = "ACME Equipment manager",
                version = "1.0.0",
                description = "Code test example",
                license = @License(name = "MIT", url = "https://foo.bar"),
                contact = @Contact(name = "Antti Järvinen", email = "ext-antti.jarvinen@futurice.com")
        )
)
public class Application {
    public static void main(String[] args) {
        Micronaut.run(Application.class);
    }
}
