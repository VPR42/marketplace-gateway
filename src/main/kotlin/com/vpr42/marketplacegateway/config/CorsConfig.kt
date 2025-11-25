package com.vpr42.marketplacegateway.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsWebFilter
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource

@Configuration
class CorsConfig {
    @Bean
    fun corsWebFilter(): CorsWebFilter {
        val config = CorsConfiguration().apply {
            allowCredentials = true

            // Явно указываем фронты
            allowedOrigins = listOf(
                "http://localhost:5173",
                "https://hack.kinoko.su"
            )

            addAllowedHeader("*")
            addAllowedMethod("*")
        }

        val source = UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration("/**", config)
        }

        return CorsWebFilter(source)
    }
}
