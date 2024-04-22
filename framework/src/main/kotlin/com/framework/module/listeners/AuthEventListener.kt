package com.framework.module.listeners

import com.framework.module.auth.entities.ActiveUserDevice
import com.framework.module.auth.repository.ActiveUserDeviceRepository
import com.framework.module.events.LoginUserEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component


@Component
class AuthEventListener(private val activeUserDeviceRepository: ActiveUserDeviceRepository) {
    @EventListener
    fun handleLoginUserEvent(loginUserEvent: LoginUserEvent) {

        activeUserDeviceRepository.deleteActiveUserDeviceByUserOrDevice(loginUserEvent.user, loginUserEvent.device)
        activeUserDeviceRepository.save(ActiveUserDevice(loginUserEvent.user, loginUserEvent.device))
    }
}