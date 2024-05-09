package com.example.userservice.config;

import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@io.swagger.v3.oas.annotations.OpenAPIDefinition(
		info=@Info(
				title = "user-service",description = "",summary = "",termsOfService = "T$C",
				contact =@Contact(
					name="vo quang huy",
							email="voquanghuy08102000@gmail.com"
					
				),
				license = @License(
						name = "huy"),
				version = "v1"),
		servers = {@Server(
				description = "dev",
				url = "http://localhost:8083")})

public class OpenAPIDefinition {

}
