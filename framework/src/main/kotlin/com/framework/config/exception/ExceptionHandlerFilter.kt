package com.framework.config.exception

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.servlet.HandlerExceptionResolver


private val kLogger = KotlinLogging.logger {}

@Component
@Order(Int.MIN_VALUE + 1)
class ExceptionHandlerFilter(val handlerExceptionResolver: HandlerExceptionResolver) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            filterChain.doFilter(request, response)
        } catch (e: Exception) {
            kLogger.error { "Filter Error $e" }
            handlerExceptionResolver.resolveException(request, response, null, e)
        }
    }
}