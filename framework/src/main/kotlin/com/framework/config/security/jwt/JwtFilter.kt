package com.framework.config.security.jwt

import com.framework.config.exception.DeviceLoggedOut
import com.framework.config.request.RequestContext
import com.framework.module.auth.entities.User
import com.framework.module.auth.repository.ActiveUserDeviceRepository
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter


@Component
class JwtAuthFilter(
    private val jwtUtils: JwtUtils,
    private val userDetailsService: UserDetailsService,
    private val requestContext: RequestContext,
    private val activeUserDeviceRepository: ActiveUserDeviceRepository
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        updateContext(request)
        checkLoggedInUserDevice()

        filterChain.doFilter(request, response)

    }

    private fun checkLoggedInUserDevice() {
        if (requestContext.currentUser != null && requestContext.currentDevice != null) {
            activeUserDeviceRepository.findByUserOrDevice(
                requestContext.currentUser!!,
                requestContext.currentDevice!!
            ) ?: throw DeviceLoggedOut()
        }
    }

    private fun updateContext(request: HttpServletRequest) {
        requestContext.jwtToken?.let { jwtToken ->
            if (SecurityContextHolder.getContext().authentication == null) {
                val foundUser = userDetailsService.loadUserByUsername(jwtUtils.getSubject(jwtToken))
                if (jwtUtils.isValid(jwtToken, foundUser)) {
                    val currentUser = foundUser as User
                    updateSecurityContext(currentUser, request)
                    requestContext.currentUser = currentUser
                }
            }
        }
    }

    private fun updateSecurityContext(currentUser: User, request: HttpServletRequest) {
        val authToken = UsernamePasswordAuthenticationToken(currentUser, null, currentUser.authorities)
        authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
        SecurityContextHolder.getContext().authentication = authToken
        MDC.put("user-id", currentUser.id.toString())
    }
}
