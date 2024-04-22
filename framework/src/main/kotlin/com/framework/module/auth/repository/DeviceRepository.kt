package com.framework.module.auth.repository

import com.framework.module.auth.entities.ActiveUserDevice
import com.framework.module.auth.entities.Device
import com.framework.module.auth.entities.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
interface DeviceRepository : JpaRepository<Device, Long> {
    fun findDeviceByDeviceUid(deviceUid: UUID): Device?
}

@Repository
interface ActiveUserDeviceRepository : JpaRepository<ActiveUserDevice, Long> {

    @Modifying
    @Transactional
    fun deleteActiveUserDeviceByUserOrDevice(user: User, device: Device)

    fun findByUserOrDevice(user: User, device: Device): ActiveUserDevice?

}