package com.vpr42.marketplacegateway.security

import com.vpr42.marketplacegateway.properties.ApplicationProperties
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Component
import org.springframework.util.AntPathMatcher
import java.util.function.Predicate

@Component
class RouterValidator(
    private val applicationProperties: ApplicationProperties
) {
    private val pathMatcher = AntPathMatcher()

    private val swaggerUrls = listOf(
        "/api/**/docs/**",
        "/api/**/swagger",
        "/api/**/swagger/**",
        "/api/**/swagger-ui/**",
    )

    val isSecured: Predicate<ServerHttpRequest> = Predicate<ServerHttpRequest> { request ->
        val path = request.uri.path

        if (isSwaggerPath(path)) {
            false
        } else {
            applicationProperties
                .routes
                .filter { it.isSecure }
                .stream()
                .noneMatch {
                    path.contains(it.uri)
                }
        }
    }

    private fun isSwaggerPath(path: String): Boolean =
        swaggerUrls.any { pattern -> pathMatcher.match(pattern, path) }
}
