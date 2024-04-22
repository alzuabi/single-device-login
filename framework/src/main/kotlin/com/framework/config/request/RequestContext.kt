package com.framework.config.request

import com.framework.config.exception.HeaderMissingException
import com.framework.config.security.jwt.JwtUtils
import com.framework.module.auth.entities.Device
import com.framework.module.auth.entities.User
import com.framework.module.auth.enums.DeviceOS
import com.framework.module.auth.enums.toDeviceOs
import com.framework.module.auth.repository.DeviceRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.ObjectProvider
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.stereotype.Component
import org.springframework.web.context.annotation.RequestScope
import java.util.*


private val kLogger = KotlinLogging.logger {}


@Component
@RequestScope(proxyMode = ScopedProxyMode.TARGET_CLASS)
class RequestContext(
    private val objectProvider: ObjectProvider<HttpServletRequest>,
    private val jwtUtils: JwtUtils,
    private val deviceRepository: DeviceRepository
) {
    var currentUser: User? = null
    var jwtToken = currentJwtToken()
    var currentDevice = currentDevice()

    private fun currentJwtToken(): String? {
        var jwtToken: String? = null
        objectProvider.ifAvailable { request ->
            jwtToken = jwtUtils.getJwtToken(request)
        }
        return jwtToken
    }

    private fun currentDevice(): Device? {
        var deviceUid: String? = null
        lateinit var deviceAgent: String

        objectProvider.ifAvailable {
            deviceAgent = it.getHeader("X-Device-Agent") ?: throw HeaderMissingException("X-Device-Agent")
            deviceUid = it.getHeader("X-Device-UID")
        }

        if (deviceAgent.toDeviceOs() == DeviceOS.BROWSER) {
            return deviceUid?.let {
                deviceRepository.findDeviceByDeviceUid(UUID.fromString(it))
            } ?: deviceRepository.save(Device(UUID.randomUUID(), DeviceOS.BROWSER))
        } else {
            if (deviceUid == null) throw HeaderMissingException("X-Device-UID")
            return deviceRepository.findDeviceByDeviceUid(UUID.fromString(deviceUid))
                ?: deviceRepository.save(Device(UUID.fromString(deviceUid), deviceAgent.toDeviceOs()))
        }
    }
}

