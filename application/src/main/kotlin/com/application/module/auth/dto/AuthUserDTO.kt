package com.application.com.application.module.auth.dto

import jakarta.validation.constraints.NotBlank


data class RegisterUserRequest(
    @field:NotBlank val username: String,
    @field:NotBlank val password: String
)

data class LoginUserRequest(
    @field:NotBlank val username: String,
    @field:NotBlank val password: String
)


data class RegisterUserResponse(val token: String)

data class LoginUserResponse(val token: String)