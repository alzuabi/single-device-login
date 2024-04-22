package com.framework.module.auth.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service


@Service
class HashingService(private val passwordEncoder: PasswordEncoder) {
    fun String.encoded(): String = passwordEncoder.encode(this)
}