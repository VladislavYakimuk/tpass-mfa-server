package com.tpass.mfa.controller

import com.tpass.mfa.dto.ChallengeApprovalRequest
import com.tpass.mfa.service.ChallengeService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/test")
class TestApprovalController(
    private val challengeService: ChallengeService
) {
    
    @GetMapping("/challenges")
    fun getPendingChallenges(): ResponseEntity<List<Map<String, Any>>> {
        // Получаем все pending challenges для тестирования
        val challenges = listOf(
            mapOf(
                "challengeId" to "test-challenge-1",
                "userId" to "user",
                "status" to "PENDING",
                "message" to "Use this endpoint to approve challenges manually"
            )
        )
        return ResponseEntity.ok(challenges)
    }
    
    @PostMapping("/approve/{challengeId}")
    fun approveChallenge(@PathVariable challengeId: String): ResponseEntity<Map<String, Any>> {
        return try {
            // Создаем фиктивный запрос на одобрение
            val request = ChallengeApprovalRequest(
                challengeId = challengeId,
                deviceId = "android_1761294588767", // Используем реальный device ID
                signature = "test-signature" // Фиктивная подпись для тестирования
            )
            
            val response = challengeService.approveChallenge(request)
            
            val result = mapOf(
                "success" to response.success,
                "message" to response.message,
                "challengeId" to challengeId
            )
            
            ResponseEntity.ok(result)
        } catch (e: Exception) {
            val error = mapOf(
                "success" to false,
                "message" to "Error: ${e.message}",
                "challengeId" to challengeId
            )
            ResponseEntity.badRequest().body(error)
        }
    }
    
    @PostMapping("/deny/{challengeId}")
    fun denyChallenge(@PathVariable challengeId: String): ResponseEntity<Map<String, Any>> {
        return try {
            val response = challengeService.denyChallenge(challengeId)
            
            val result = mapOf(
                "success" to response.success,
                "message" to response.message,
                "challengeId" to challengeId
            )
            
            ResponseEntity.ok(result)
        } catch (e: Exception) {
            val error = mapOf(
                "success" to false,
                "message" to "Error: ${e.message}",
                "challengeId" to challengeId
            )
            ResponseEntity.badRequest().body(error)
        }
    }
}
