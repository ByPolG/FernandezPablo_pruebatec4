package com.app;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AppApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI().info(new Info()
				.title("API de Agencia de Viajes - Gestión de Vuelos y Hoteles")
				.version("1.1.2")

				.description("¡Bienvenido a la API de nuestra agencia de viajes! " +
						"Aquí puedes gestionar reservas de vuelos y hoteles, explorar " +
						"opciones de alojamiento y vuelos, ¡y mucho más!"));
	}

}
