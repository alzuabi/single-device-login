package com.framework.module.auth.enums

enum class RoleName {
    ROLE_CUSTOMER, ROLE_ADMIN, ROLE_GENERAL
}

enum class DeviceOS {
    ANDROID, IPHONE, BROWSER
}

public fun String.toDeviceOs(): DeviceOS = DeviceOS.valueOf(this)
