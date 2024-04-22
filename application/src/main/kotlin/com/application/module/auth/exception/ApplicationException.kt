package com.application.com.application.module.auth.exception

import com.framework.config.exception.ApplicationException
import org.springframework.http.HttpStatus

class UserAlreadyExistException : ApplicationException(HttpStatus.CONFLICT, "USER-0001", "User Already Exist")

