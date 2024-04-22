package com.framework.module.utils

import com.framework.module.auth.service.BaseResponse
import com.framework.module.auth.service.FailedResponseBody
import org.springframework.http.HttpStatus

fun <T> T.success(httpStatus: HttpStatus = HttpStatus.OK): BaseResponse.SuccessResponse<T> =
    BaseResponse.SuccessResponse(this, httpStatus)

fun <T> T.fail(
    errorCode: String = "",
    message: String,
    httpStatus: HttpStatus = HttpStatus.BAD_REQUEST
): BaseResponse.FailedResponse<FailedResponseBody> =
    BaseResponse.FailedResponse(FailedResponseBody(errorCode, message), httpStatus)