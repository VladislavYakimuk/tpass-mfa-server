package com.tpass.mfa.dto

import com.tpass.mfa.model.ChallengeStatus
import jakarta.validation.constraints.NotBlank
import java.time.LocalDateTime

data class CreateChallengeRequest(
    @field:NotBlank(message = "User ID is required")
    val userId: String,
    
    val nasIp: String? = null,
    val callerId: String? = null
)

data class CreateChallengeResponse(
    val success: Boolean,
    val challengeId: String? = null,
    val challengeData: String? = null,
    val expiresAt: LocalDateTime? = null,
    val message: String? = null
)

data class ChallengeResponse(
    val challengeId: String,
    val challengeData: String,
    val status: ChallengeStatus,
    val createdAt: LocalDateTime,
    val expiresAt: LocalDateTime,
    val nasIp: String? = null,
    val callerId: String? = null
)

data class ChallengeApprovalRequest(
    @field:NotBlank(message = "Challenge ID is required")
    val challengeId: String,
    
    @field:NotBlank(message = "Device ID is required")
    val deviceId: String,
    
    @field:NotBlank(message = "Signature is required")
    val signature: String
)

data class ChallengeApprovalResponse(
    val success: Boolean,
    val message: String
)
