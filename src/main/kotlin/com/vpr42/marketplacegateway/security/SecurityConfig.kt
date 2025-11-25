package com.vpr42.marketplacegateway.security

import com.vpr42.marketplace.jooq.tables.pojos.Users
import com.vpr42.marketplacegateway.exception.UserNotFoundException
import com.vpr42.marketplacegateway.repository.UserRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
class SecurityConfig(
    private val userRepository: UserRepository
) {

    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain =
        http
            .csrf { it.disable() }
            .cors {  }
            .httpBasic { it.disable() }
            .formLogin { it.disable() }
            .logout { it.disable() }
            .authorizeExchange { exchanges ->
                exchanges
                    .anyExchange()
                    .permitAll()
            }
            .build()

    @Bean
    fun userDetailsService() = UserDetailsService { login: String ->
        userRepository.findByEmail(login)?.toUserAuthDetails()
            ?: throw UserNotFoundException("with login $login")
    }

    private fun Users.toUserAuthDetails() = UserAuthDetails(
        login = this.email,
        authPassword = this.password,
    )

    data class UserAuthDetails(
        val login: String,
        val authPassword: String,
    ) : UserDetails {
        override fun getUsername() = this.login
        override fun getPassword() = this.authPassword
        override fun getAuthorities() = listOf(SimpleGrantedAuthority("USER"))
    }
}
