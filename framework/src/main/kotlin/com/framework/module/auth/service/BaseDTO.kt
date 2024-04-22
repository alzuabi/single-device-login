package com.framework.module.auth.service

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

sealed class BaseResponse<T>(body: T, httpStatus: HttpStatus) : ResponseEntity<T>(body, httpStatus) {
    data class FailedResponse<T : FailedResponseBody>(
        private val body: T,
        private val httpStatus: HttpStatus = HttpStatus.BAD_REQUEST
    ) : BaseResponse<T>(body, httpStatus)

    data class SuccessResponse<T>(
        private val body: T,
        private val httpStatus: HttpStatus = HttpStatus.OK,
    ) : BaseResponse<T>(body, httpStatus)


}

open class FailedResponseBody(val errorCode: String, val message: String)
