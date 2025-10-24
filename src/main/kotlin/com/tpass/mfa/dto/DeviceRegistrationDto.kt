package com.tpass.mfa.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class DeviceRegistrationRequest(
    @field:NotBlank(message = "Device ID is required")
    val deviceId: String,
    
    @field:NotBlank(message = "User ID is required")
    val userId: String,
    
    @field:NotBlank(message = "Device name is required")
    val deviceName: String,
    
    @field:NotBlank(message = "Public key is required")
    val publicKey: String,
    
    val fcmToken: String? = null
)

data class DeviceRegistrationResponse(
    val success: Boolean,
    val message: String,
    val deviceId: String? = null
)
