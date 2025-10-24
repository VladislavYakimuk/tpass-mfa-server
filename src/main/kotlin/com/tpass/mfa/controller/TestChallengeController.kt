package com.tpass.mfa.controller

import com.tpass.mfa.dto.ChallengeApprovalRequest
import com.tpass.mfa.service.ChallengeService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/test-challenge")
class TestChallengeController(
    private val challengeService: ChallengeService
) {
    
    @PostMapping("/create")
    fun createChallenge(@RequestBody request: Map<String, String>): ResponseEntity<Map<String, Any>> {
        println("=== CREATE CHALLENGE ===")
        println("User ID: ${request["userId"]}")
        println("NAS IP: ${request["nasIp"]}")
        println("Caller ID: ${request["callerId"]}")
        
        val userId = request["userId"]
        val nasIp = request["nasIp"]
        val callerId = request["callerId"]
        
        if (userId.isNullOrBlank()) {
            return ResponseEntity.badRequest().body(mapOf(
                "success" to false,
                "message" to "User ID is required"
            ))
        }
        
        try {
            val challengeRequest = com.tpass.mfa.dto.CreateChallengeRequest(
                userId = userId,
                nasIp = nasIp ?: "",
                callerId = callerId ?: ""
            )
            
            val response = challengeService.createChallenge(challengeRequest)
            
            if (response.success) {
                println("Challenge created successfully: ${response.challengeId}")
                return ResponseEntity.ok(mapOf(
                    "success" to true,
                    "message" to "Challenge created successfully",
                    "challengeId" to (response.challengeId ?: "")
                ))
            } else {
                println("Failed to create challenge: ${response.message}")
                return ResponseEntity.badRequest().body(mapOf(
                    "success" to false,
                    "message" to (response.message ?: "Unknown error")
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
    
    @GetMapping("/status/{challengeId}")
    fun getChallengeStatus(@PathVariable challengeId: String): ResponseEntity<Map<String, Any>> {
        println("=== GET CHALLENGE STATUS ===")
        println("Challenge ID: $challengeId")
        
        try {
            val status = challengeService.getChallengeStatus(challengeId)
            
            if (status != null) {
                println("Challenge status: ${status.status}")
                return ResponseEntity.ok(mapOf(
                    "success" to true,
                    "challengeId" to challengeId,
                    "status" to status.status.toString(),
                    "createdAt" to status.createdAt,
                    "expiresAt" to status.expiresAt
                ))
            } else {
                println("Challenge not found: $challengeId")
                return ResponseEntity.badRequest().body(mapOf(
                    "success" to false,
                    "message" to "Challenge not found"
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
    
    @PostMapping("/approve")
    fun approveChallenge(@RequestBody request: Map<String, String>): ResponseEntity<Map<String, Any>> {
        println("=== APPROVE CHALLENGE ===")
        println("Challenge ID: ${request["challengeId"]}")
        println("Device ID: ${request["deviceId"]}")
        println("Signature: ${request["signature"]?.take(50)}...")
        
        val challengeId = request["challengeId"]
        val deviceId = request["deviceId"]
        val signature = request["signature"]
        
        if (challengeId.isNullOrBlank() || deviceId.isNullOrBlank() || signature.isNullOrBlank()) {
            return ResponseEntity.badRequest().body(mapOf(
                "success" to false,
                "message" to "Challenge ID, Device ID, and Signature are required"
            ))
        }
        
        try {
            val approvalRequest = ChallengeApprovalRequest(
                challengeId = challengeId,
                deviceId = deviceId,
                signature = signature
            )
            
            val response = challengeService.approveChallenge(approvalRequest)
            
            if (response.success) {
                println("Challenge approved successfully: $challengeId")
                return ResponseEntity.ok(mapOf(
                    "success" to true,
                    "message" to "Challenge approved successfully"
                ))
            } else {
                println("Failed to approve challenge: ${response.message}")
                return ResponseEntity.badRequest().body(mapOf(
                    "success" to false,
                    "message" to (response.message ?: "Unknown error")
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
    
    @PostMapping("/deny/{challengeId}")
    fun denyChallenge(@PathVariable challengeId: String): ResponseEntity<Map<String, Any>> {
        println("=== DENY CHALLENGE ===")
        println("Challenge ID: $challengeId")
        
        try {
            val response = challengeService.denyChallenge(challengeId)
            
            if (response.success) {
                println("Challenge denied successfully: $challengeId")
                return ResponseEntity.ok(mapOf(
                    "success" to true,
                    "message" to "Challenge denied successfully"
                ))
            } else {
                println("Failed to deny challenge: ${response.message}")
                return ResponseEntity.badRequest().body(mapOf(
                    "success" to false,
                    "message" to (response.message ?: "Unknown error")
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
}
