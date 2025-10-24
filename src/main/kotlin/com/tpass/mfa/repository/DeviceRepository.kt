package com.tpass.mfa.repository

import com.tpass.mfa.model.Device
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface DeviceRepository : JpaRepository<Device, Long> {
    fun findByDeviceId(deviceId: String): Optional<Device>
    fun findByUserId(userId: String): List<Device>
    fun findByUserIdAndIsActiveTrue(userId: String): List<Device>
}
