package com.vpr42.marketplacegateway.security

import com.vpr42.marketplace.jooq.tables.pojos.Users
import com.vpr42.marketplacegateway.exception.UserNotFoundException
import com.vpr42.marketplacegateway.repository.UserRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val userRepository: UserRepository
) {
    @Bean
    fun userDetailsService() = UserDetailsService { login: String ->
        userRepository.findByEmail(login)?.toUserAuthDetails()
            ?: throw UserNotFoundException("with login $login")
    }

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

    @Bean
    fun authenticationProvider(
        userDetailsService: UserDetailsService,
        passwordEncoder: BCryptPasswordEncoder,
    ): AuthenticationProvider = DaoAuthenticationProvider(userDetailsService).apply {
        setPasswordEncoder(passwordEncoder)
    }

    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager =
        authenticationConfiguration.authenticationManager

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
