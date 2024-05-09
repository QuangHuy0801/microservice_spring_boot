package com.example.productservice.config;

import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@io.swagger.v3.oas.annotations.OpenAPIDefinition(
		info=@Info(
				title = "product-service",description = "",summary = "",termsOfService = "HHH",
				contact =@Contact(
					name="vo quang huy",
							email="voquanghuy08102000@gmail.com"
					
				),
				license = @License(
						name = "huy"),
				version = "v1"),
		servers = {@Server(
				description = "dev",
				url = "http://localhost:8082")})

public class OpenAPIDefinition {

}
