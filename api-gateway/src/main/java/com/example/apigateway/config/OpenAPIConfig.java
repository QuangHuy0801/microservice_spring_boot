package com.example.apigateway.config;


import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class OpenAPIConfig{

	 @Bean
	    public List<GroupedOpenApi> apis(RouteDefinitionLocator locator) {
	        Flux<RouteDefinition> routeDefinitions = locator.getRouteDefinitions();
	        return routeDefinitions
	                .filter(routeDefinition -> routeDefinition.getId().matches(".*-service"))
	                .map(routeDefinition -> routeDefinition.getId().replaceAll("-service", ""))
	                .distinct()
	                .map(serviceName -> GroupedOpenApi.builder().pathsToMatch("/" + serviceName + "/**").group(serviceName).build())
	                .collect(Collectors.toList())
	                .block();
	    }
}
