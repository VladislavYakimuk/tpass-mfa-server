package com.tpass.mfa.controller

import com.tpass.mfa.dto.DeviceRegistrationRequest
import com.tpass.mfa.service.DeviceService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/test-device")
class TestDeviceController(
    private val deviceService: DeviceService
) {
    
    @PostMapping("/register")
    fun registerDevice(@RequestBody request: DeviceRegistrationRequest): ResponseEntity<Map<String, Any>> {
        println("=== DEVICE REGISTRATION ===")
        println("Device ID: ${request.deviceId}")
        println("User ID: ${request.userId}")
        println("Device Name: ${request.deviceName}")
        println("Public Key: ${request.publicKey?.take(50)}...")
        
        try {
            val response = deviceService.registerDevice(request)
            
            if (response.success) {
                println("Device registered successfully: ${request.deviceId}")
                return ResponseEntity.ok(mapOf(
                    "success" to true,
                    "message" to "Device registered successfully",
                    "deviceId" to request.deviceId,
                    "userId" to request.userId
                ))
            } else {
                println("Failed to register device: ${response.message}")
                return ResponseEntity.badRequest().body(mapOf(
                    "success" to false,
                    "message" to response.message
                ))
            }
        } catch (e: Exception) {
            println("ERROR: ${e.message}")
            return ResponseEntity.badRequest().body(mapOf(
                "success" to false,
                "message" to "Internal error: ${e.message}"
            ))
        }
    }
    
    @GetMapping("/devices/{userId}")
    fun getDevices(@PathVariable userId: String): ResponseEntity<Map<String, Any>> {
        println("=== GET DEVICES ===")
        println("User ID: $userId")
        
        try {
            val devices = deviceService.getDevicesByUserId(userId)
            println("Found ${devices.size} devices for user: $userId")
            
            return ResponseEntity.ok(mapOf(
                "success" to true,
                "devices" to devices.map { device ->
                    mapOf(
                        "deviceId" to device.deviceId,
                        "deviceName" to device.deviceName,
                        "isActive" to device.isActive,
                        "createdAt" to device.createdAt
                    )
                }
            ))
        } catch (e: Exception) {
            println("ERROR: ${e.message}")
            return ResponseEntity.badRequest().body(mapOf(
                "success" to false,
                "message" to "Internal error: ${e.message}"
            ))
        }
    }
}
