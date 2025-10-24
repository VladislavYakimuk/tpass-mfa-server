package com.tpass.mfa.controller

import com.tpass.mfa.dto.DeviceRegistrationRequest
import com.tpass.mfa.dto.DeviceRegistrationResponse
import com.tpass.mfa.service.DeviceService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/devices")
class DeviceController(
    private val deviceService: DeviceService
) {
    
    @PostMapping("/register")
    fun registerDevice(@Valid @RequestBody request: DeviceRegistrationRequest): ResponseEntity<DeviceRegistrationResponse> {
        val response = deviceService.registerDevice(request)
        return if (response.success) {
            ResponseEntity.ok(response)
        } else {
            ResponseEntity.badRequest().body(response)
        }
    }
    
    @GetMapping("/user/{userId}")
    fun getDevicesByUserId(@PathVariable userId: String): ResponseEntity<List<com.tpass.mfa.model.Device>> {
        val devices = deviceService.getDevicesByUserId(userId)
        return ResponseEntity.ok(devices)
    }
    
    @GetMapping("/{deviceId}")
    fun getDevice(@PathVariable deviceId: String): ResponseEntity<com.tpass.mfa.model.Device> {
        val device = deviceService.getDeviceById(deviceId)
        return if (device.isPresent) {
            ResponseEntity.ok(device.get())
        } else {
            ResponseEntity.notFound().build()
        }
    }
    
    @PutMapping("/{deviceId}/fcm-token")
    fun updateFcmToken(
        @PathVariable deviceId: String,
        @RequestBody fcmToken: String
    ): ResponseEntity<Map<String, Any>> {
        val success = deviceService.updateFcmToken(deviceId, fcmToken)
        return if (success) {
            ResponseEntity.ok(mapOf("success" to true, "message" to "FCM token updated"))
        } else {
            ResponseEntity.badRequest().body(mapOf("success" to false, "message" to "Device not found"))
        }
    }
}
