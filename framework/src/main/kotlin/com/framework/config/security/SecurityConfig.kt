package com.framework.config.security

import com.framework.config.security.jwt.JwtAuthFilter
import com.framework.module.auth.repository.UserRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig {

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()


    @Bean
    fun userDetailsService(userRepository: UserRepository): UserDetailsService {

        return UserDetailsService { username: String? ->
            username?.let {
                userRepository.findByUsername(it) ?: throw UsernameNotFoundException("User Not Found")
            }
        }

    }

    @Bean
    fun authenticationProvider(
        userDetailsService: UserDetailsService,
        passwordEncoder: PasswordEncoder
    ): AuthenticationProvider {
        return DaoAuthenticationProvider().apply {
            setPasswordEncoder(passwordEncoder)
            setUserDetailsService(userDetailsService)
        }
    }

    @Bean
    fun authenticationManager(
        httpSecurity: HttpSecurity,
        authenticationProvider: AuthenticationProvider,
    ): AuthenticationManager {
        return httpSecurity
            .getSharedObject(AuthenticationManagerBuilder::class.java)
            .authenticationProvider(authenticationProvider).build()
    }

    @Bean
    @Profile("!no-auth")
    fun securityFilterChain(
        httpSecurity: HttpSecurity,
        jwtAuthFilter: JwtAuthFilter
    ): SecurityFilterChain {
        httpSecurity {

            csrf { disable() }

            headers { frameOptions { disable() } }

            sessionManagement { sessionCreationPolicy = SessionCreationPolicy.STATELESS }

            httpBasic { }

            formLogin { }

            authorizeHttpRequests {
                authorize(antMatcher("/h2-console/**"), permitAll)
                authorize("/api/v1/auth/register", permitAll)
                authorize("/api/v1/auth/login", permitAll)
                authorize("/error", permitAll)
                authorize(anyRequest, authenticated)
            }

            addFilterBefore<UsernamePasswordAuthenticationFilter>(jwtAuthFilter)

            exceptionHandling { }
        }
        return httpSecurity.build()
    }

    @Bean
    @Profile("no-auth")
    fun securityFilterChainNoSec(
        httpSecurity: HttpSecurity
    ): SecurityFilterChain {
        httpSecurity {
            csrf { disable() }
            headers { frameOptions { disable() } }
            sessionManagement { sessionCreationPolicy = SessionCreationPolicy.STATELESS }

            authorizeHttpRequests {
                authorize(anyRequest, permitAll)
            }
            httpBasic { }
            formLogin { }
        }
        return httpSecurity.build()
    }
}