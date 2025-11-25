package com.vpr42.marketplacegateway

import com.vpr42.marketplacegateway.properties.ApplicationProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.web.reactive.config.EnableWebFlux

@SpringBootApplication
@EnableDiscoveryClient
@EnableWebFlux
@EnableConfigurationProperties(
    ApplicationProperties::class
)
class MarketplaceGatewayApplication

fun main(args: Array<String>) {
    runApplication<MarketplaceGatewayApplication>(*args)
}
