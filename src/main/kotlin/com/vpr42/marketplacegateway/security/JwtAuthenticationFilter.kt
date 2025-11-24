package com.vpr42.marketplacegateway.security

import org.springframework.cloud.context.config.annotation.RefreshScope
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.http.HttpStatus
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@RefreshScope
@Component
class JwtAuthenticationFilter(
    private val jwtService: JwtService,
    private val userDetailsService: UserDetailsService,
    private val routerValidator: RouterValidator
) : GatewayFilter {

    override fun filter(
        exchange: ServerWebExchange,
        chain: GatewayFilterChain
    ): Mono<Void?>? {
        val request: ServerHttpRequest = exchange.request

        if (routerValidator.isSecured.test(request)) {
            if (!request.headers.containsKey(AUTH_HEADER)) return onError(exchange)

            val token = request
                .headers
                .getOrEmpty(AUTH_HEADER)[0]
                .substring(7)
            val userDetails = userDetailsService.loadUserByUsername(jwtService.getLogin(token))

            if (!jwtService.isTokenValid(token, userDetails)) return onError(exchange)

            populateRequestWithHeaders(exchange, token)
        }

        return chain.filter(exchange)
    }

    private fun onError(exchange: ServerWebExchange): Mono<Void?> {
        val response: ServerHttpResponse = exchange.response
        response.statusCode = HttpStatus.UNAUTHORIZED
        return response.setComplete()
    }

    private fun populateRequestWithHeaders(exchange: ServerWebExchange, token: String) {
        exchange.request.mutate()
            .header("email", jwtService.getLogin(token))
            .header("id", jwtService.getId(token))
            .build()
    }

    companion object {
        private const val AUTH_HEADER = "Authorization"
    }
}
