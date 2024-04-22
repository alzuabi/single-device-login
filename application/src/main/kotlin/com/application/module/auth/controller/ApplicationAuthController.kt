package com.application.module.auth.controller


import com.application.com.application.module.auth.dto.LoginUserRequest
import com.application.com.application.module.auth.dto.LoginUserResponse
import com.application.com.application.module.auth.dto.RegisterUserRequest
import com.application.com.application.module.auth.dto.RegisterUserResponse
import com.application.com.application.module.auth.service.ApplicationAuthService
import com.application.com.application.module.auth.validator.ApplicationAuthValidator
//import com.framework.config.request.RequestProperties
import com.framework.module.auth.service.BaseResponse
import com.framework.module.utils.success
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

private val kLogger = KotlinLogging.logger {}

@RestController
@RequestMapping("/api/v1/auth")
class ApplicationAuthController(
    private val applicationAuthService: ApplicationAuthService,
    private val applicationAuthValidator: ApplicationAuthValidator,
) {

    @PostMapping("/register")
    fun register(@Valid @RequestBody registerUserRequest: RegisterUserRequest): BaseResponse<RegisterUserResponse> {
        applicationAuthValidator.validateRegister(registerUserRequest)
        return applicationAuthService.registerUser(registerUserRequest).success(HttpStatus.CREATED)
    }

    @PostMapping("/login")
    fun login(@Valid @RequestBody loginUserRequest: LoginUserRequest): BaseResponse<LoginUserResponse> {
        return applicationAuthService.login(loginUserRequest).success()
    }

    @GetMapping("/dummy")
    fun dummy(): BaseResponse<Map<String, Any>> {
        return mapOf("test" to 1).success()
    }

}




