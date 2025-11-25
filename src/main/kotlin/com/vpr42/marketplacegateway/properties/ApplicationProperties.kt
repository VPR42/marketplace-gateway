package com.vpr42.marketplacegateway.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("app")
data class ApplicationProperties(
    val auth: AuthenticationProperties,
    val routes: List<Route> = listOf(),
) {
    data class AuthenticationProperties(
        val secret: String,
    )

    data class Route(
        val id: String,
        val path: String,
        val uri: String,
        val isSecure: Boolean = true,
    )
}
