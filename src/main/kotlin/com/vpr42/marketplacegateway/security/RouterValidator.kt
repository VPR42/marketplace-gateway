package com.vpr42.marketplacegateway.security

import com.vpr42.marketplacegateway.properties.ApplicationProperties
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Component
import java.util.function.Predicate

@Component
class RouterValidator(
    private val applicationProperties: ApplicationProperties
) {
    var isSecured: Predicate<ServerHttpRequest> = Predicate<ServerHttpRequest> { request ->
        applicationProperties
            .routes
            .filter { !it.isSecure }
            .stream()
            .noneMatch {
                request.uri.path.contains(it.uri)
            }
    }
}
