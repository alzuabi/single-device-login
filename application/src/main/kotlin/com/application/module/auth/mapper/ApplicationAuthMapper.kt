package com.application.com.application.module.auth.mapper

import com.application.com.application.module.auth.dto.RegisterUserRequest
import com.framework.module.auth.entities.User
import com.framework.module.auth.service.HashingService
import com.framework.module.auth.service.UserService
import org.springframework.stereotype.Service

@Service
class ApplicationAuthMapper(private val userService: UserService, private val hashingService: HashingService) {
    fun toUser(registerUserRequest: RegisterUserRequest) = User(
        registerUserRequest.username,
        with(hashingService) { registerUserRequest.password.encoded() },
        roles = setOf(userService.getRole())
    )
}