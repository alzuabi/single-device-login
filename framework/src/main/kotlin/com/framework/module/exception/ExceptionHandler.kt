package com.framework.config.exception

import com.framework.module.auth.service.BaseResponse
import com.framework.module.auth.service.FailedResponseBody
import io.github.oshai.kotlinlogging.KotlinLogging
import org.apache.commons.lang3.exception.ExceptionUtils
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler


private val kLogger = KotlinLogging.logger {}

open class ApplicationException(val httpStatus: HttpStatus, val errorCode: String, override val message: String?) :
    RuntimeException()

class HeaderMissingException(headerName: String) :
    ApplicationException(HttpStatus.BAD_REQUEST, "HEADER-0001", "$headerName Is Missing")

class DeviceLoggedOut :
    ApplicationException(HttpStatus.UNAUTHORIZED, "DEVICE-0001", "Device logged out, Please login")


@ControllerAdvice
class ExceptionHandler : ResponseEntityExceptionHandler() {
    @ExceptionHandler(value = [ApplicationException::class])
    fun handleApplicationException(applicationException: ApplicationException): BaseResponse.FailedResponse<FailedResponseBody> {
        return BaseResponse.FailedResponse(applicationException.toFailedResponseBody(), applicationException.httpStatus)
    }

    @ExceptionHandler(value = [Throwable::class])
    fun handleException(throwable: Throwable): BaseResponse.FailedResponse<FailedResponseBody> {
        kLogger.error { throwable.stackTraceToString() }
        return BaseResponse.FailedResponse(
            ExceptionUtils.getRootCause(throwable).toFailedResponseBody(),
            HttpStatus.BAD_REQUEST
        )
    }

}

private fun Throwable.toFailedResponseBody() = FailedResponseBody("GENERIC-ERROR-0000", this.message.orEmpty())

private fun ApplicationException.toFailedResponseBody(): FailedResponseBody =
    FailedResponseBody(errorCode, message.orEmpty())


