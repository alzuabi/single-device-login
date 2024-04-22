package com.application.com.application.module.auth.service

import com.application.com.application.module.auth.dto.LoginUserRequest
import com.application.com.application.module.auth.dto.LoginUserResponse
import com.application.com.application.module.auth.dto.RegisterUserRequest
import com.application.com.application.module.auth.dto.RegisterUserResponse
import com.application.com.application.module.auth.mapper.ApplicationAuthMapper
import com.framework.config.request.RequestContext
import com.framework.config.security.jwt.JwtUtils
import com.framework.module.auth.service.UserService
import com.framework.module.events.LoginUserEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class ApplicationAuthService(
    private val userService: UserService,
    private val mapperService: ApplicationAuthMapper,
    private val jwtUtils: JwtUtils,
    private val applicationEventPublisher: ApplicationEventPublisher,
    private val requestContext: RequestContext,
) {
    fun registerUser(registerUserRequest: RegisterUserRequest): RegisterUserResponse {

        val user = mapperService.toUser(registerUserRequest)
        userService.saveUser(user)
        val token = jwtUtils.generateJwtToken(user)
        return RegisterUserResponse(token)
    }

    fun login(loginUserRequest: LoginUserRequest): LoginUserResponse {

        userService.authenticateUser(loginUserRequest.username, loginUserRequest.password)
        val user = userService.findByUsername(loginUserRequest.username)
            ?: throw UsernameNotFoundException("Authentication Failed")
        val token = jwtUtils.generateJwtToken(user)
        val device = requestContext.currentDevice
        applicationEventPublisher.publishEvent(LoginUserEvent(this, user, device!!))
        return LoginUserResponse(token)
    }

}