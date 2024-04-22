package com.framework.config.logging

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.FilterChain
import jakarta.servlet.ReadListener
import jakarta.servlet.ServletInputStream
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.util.StreamUtils
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper
import java.io.ByteArrayInputStream


private val kLogger = KotlinLogging.logger {}

@Component
@Order(Int.MIN_VALUE)
class LoggingFilter : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val requestWrapper = object : ContentCachingRequestWrapper(request) {
            val byteArray = StreamUtils.copyToByteArray(request.inputStream)
            override fun getInputStream() = CustomServletInputStream(byteArray)
        }

        val responseWrapper = ContentCachingResponseWrapper(response)

        requestWrapper.log()

        filterChain.doFilter(requestWrapper, responseWrapper)

        responseWrapper.log()

    }
}


private fun ContentCachingRequestWrapper.log() {
    kLogger.info { "[$remoteAddr] --> URL: [$method] [$requestURI] [${queryString ?: ""}] [${parameterMap.toList()}]" }

    kLogger.info {
        "[$remoteAddr] --> Headers: [${
            headerNames.toList().map { mapOf(it to getHeaders(it).toList()) }.toList()
        }]"
    }

    kLogger.info { "[$remoteAddr] --> Body: [${String(inputStream.readAllBytes())}]" }
}

private fun ContentCachingResponseWrapper.log() {

    kLogger.info { "<--  Headers: [${headerNames.toList().map { mapOf(it to getHeaders(it).toList()) }.toList()}]" }

    kLogger.info { "<--  Body: [${String(contentAsByteArray)}]" }

    copyBodyToResponse()


}


class CustomServletInputStream(byteArray: ByteArray) : ServletInputStream() {
    private val byteArrayInputStream = ByteArrayInputStream(byteArray)
    override fun read() = byteArrayInputStream.read()

    override fun isFinished(): Boolean = runCatching { byteArrayInputStream.available() == 0 }.getOrElse { false }

    override fun isReady() = true

    override fun setReadListener(p0: ReadListener?) = throw UnsupportedOperationException()


}