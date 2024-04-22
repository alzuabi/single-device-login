package com.application.com.application.module.auth.validator

import com.application.com.application.module.auth.dto.RegisterUserRequest
import com.application.com.application.module.auth.exception.UserAlreadyExistException
import com.framework.module.auth.service.UserService
import org.springframework.stereotype.Service

@Service
class ApplicationAuthValidator(private val userService: UserService) {
    fun validateRegister(registerUserRequest: RegisterUserRequest) {
        if (userService.existsUserByUsername(registerUserRequest.username))
            throw UserAlreadyExistException()
    }
}