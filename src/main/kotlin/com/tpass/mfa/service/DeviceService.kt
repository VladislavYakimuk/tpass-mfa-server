package com.tpass.mfa.service

import com.tpass.mfa.dto.DeviceRegistrationRequest
import com.tpass.mfa.dto.DeviceRegistrationResponse
import com.tpass.mfa.model.Device
import com.tpass.mfa.repository.DeviceRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class DeviceService(
    private val deviceRepository: DeviceRepository
) {
    
    fun registerDevice(request: DeviceRegistrationRequest): DeviceRegistrationResponse {
        // Проверяем, не зарегистрировано ли уже устройство
        val existingDevice = deviceRepository.findByDeviceId(request.deviceId)
        
        if (existingDevice.isPresent) {
            val device = existingDevice.get()
            if (device.isActive) {
                return DeviceRegistrationResponse(
                    success = false,
                    message = "Device already registered"
                )
            } else {
                // Реактивируем устройство
                device.copy(
                    userId = request.userId,
                    deviceName = request.deviceName,
                    publicKey = request.publicKey,
                    fcmToken = request.fcmToken,
                    isActive = true,
                    lastSeenAt = java.time.LocalDateTime.now()
                ).let { deviceRepository.save(it) }
                
                return DeviceRegistrationResponse(
                    success = true,
                    message = "Device reactivated",
                    deviceId = request.deviceId
                )
            }
        }
        
        // Создаем новое устройство
        val device = Device(
            deviceId = request.deviceId,
            userId = request.userId,
            deviceName = request.deviceName,
            publicKey = request.publicKey,
            fcmToken = request.fcmToken
        )
        
        deviceRepository.save(device)
        
        return DeviceRegistrationResponse(
            success = true,
            message = "Device registered successfully",
            deviceId = request.deviceId
        )
    }
    
    fun getDevicesByUserId(userId: String): List<Device> {
        return deviceRepository.findByUserIdAndIsActiveTrue(userId)
    }
    
    fun getDeviceById(deviceId: String): Optional<Device> {
        return deviceRepository.findByDeviceId(deviceId)
    }
    
    fun updateFcmToken(deviceId: String, fcmToken: String): Boolean {
        val device = deviceRepository.findByDeviceId(deviceId)
        if (device.isPresent) {
            val updatedDevice = device.get().copy(
                fcmToken = fcmToken,
                lastSeenAt = java.time.LocalDateTime.now()
            )
            deviceRepository.save(updatedDevice)
            return true
        }
        return false
    }
}
