package com.vpr42.marketplacegateway.config

import com.vpr42.marketplacegateway.properties.ApplicationProperties
import com.vpr42.marketplacegateway.security.JwtAuthenticationFilter
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GatewayConfig(
    private val applicationProperties: ApplicationProperties,
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
) {

    @Bean
    fun routes(builder: RouteLocatorBuilder) = builder
        .routes()
        .apply {
            applicationProperties.routes.forEach { route ->
                this.route(route.id) { r ->
                    if (route.isSecure) {
                        r.path(route.path)
                            .filters { f -> f.filter(jwtAuthenticationFilter) }
                            .uri(route.uri)
                    } else {
                        r.path(route.path).uri(route.uri)
                    }
                }
            }
        }
        .build()
        ?: throw ExceptionInInitializerError("Error in routes initialization")
}
