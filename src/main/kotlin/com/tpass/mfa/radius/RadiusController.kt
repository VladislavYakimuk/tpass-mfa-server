package com.tpass.mfa.radius

import com.tpass.mfa.dto.CreateChallengeRequest
import com.tpass.mfa.service.ChallengeService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/radius")
class RadiusController(
    private val challengeService: ChallengeService
) {
    data class RadiusReq(val user: String?, val nas_ip: String?, val caller: String?, val request_id: String?)
    data class RadiusResp(val result: String, val reason: String? = null, val challengeId: String? = null)
    
    @PostMapping("/mfa")
    fun mfa(@RequestBody req: RadiusReq): ResponseEntity<RadiusResp> {
        println("=== RADIUS MFA REQUEST ===")
        println("User: ${req.user}")
        println("NAS IP: ${req.nas_ip}")
        println("Caller: ${req.caller}")
        println("Request ID: ${req.request_id}")
        
        val user = req.user
        val nasIp = req.nas_ip
        val caller = req.caller
        
        if (user.isNullOrBlank()) {
            println("ERROR: No user provided")
            return ResponseEntity.badRequest().body(RadiusResp("reject", "No user provided"))
        }
        
        try {
            // Создаем challenge через ChallengeService
            val challengeRequest = CreateChallengeRequest(
                userId = user,
                nasIp = nasIp,
                callerId = caller
            )
            
            val challengeResponse = challengeService.createChallenge(challengeRequest)
            
            if (!challengeResponse.success) {
                println("ERROR: Failed to create challenge: ${challengeResponse.message}")
                return ResponseEntity.ok(RadiusResp("reject", challengeResponse.message))
            }
            
            val challengeId = challengeResponse.challengeId!!
            println("Challenge created: $challengeId")
            
            // Ждем ответа от пользователя (максимум 30 секунд)
            val startTime = System.currentTimeMillis()
            val timeout = 30000L // 30 секунд
            
            while (System.currentTimeMillis() - startTime < timeout) {
                val status = challengeService.getChallengeStatus(challengeId)
                
                when (status?.status) {
                    com.tpass.mfa.model.ChallengeStatus.APPROVED -> {
                        println("Challenge approved by user: $user")
                        return ResponseEntity.ok(RadiusResp("approve", "User approved", challengeId))
                    }
                    com.tpass.mfa.model.ChallengeStatus.DENIED -> {
                        println("Challenge denied by user: $user")
                        return ResponseEntity.ok(RadiusResp("reject", "User denied", challengeId))
                    }
                    com.tpass.mfa.model.ChallengeStatus.EXPIRED -> {
                        println("Challenge expired for user: $user")
                        return ResponseEntity.ok(RadiusResp("reject", "Challenge expired", challengeId))
                    }
                    else -> {
                        // Ждем еще
                        Thread.sleep(1000)
                    }
                }
            }
            
            // Таймаут
            println("Timeout waiting for user response: $user")
            return ResponseEntity.ok(RadiusResp("reject", "Timeout waiting for user response", challengeId))
            
        } catch (e: Exception) {
            println("ERROR: ${e.message}")
            return ResponseEntity.ok(RadiusResp("reject", "Internal error: ${e.message}"))
        }
    }
}
