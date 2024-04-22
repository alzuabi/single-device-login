package com.framework.module.events

import com.framework.module.auth.entities.Device
import com.framework.module.auth.entities.User
import org.springframework.context.ApplicationEvent

class LoginUserEvent(
    source: Any,
    val user: User,
    val device: Device
) : ApplicationEvent(source)