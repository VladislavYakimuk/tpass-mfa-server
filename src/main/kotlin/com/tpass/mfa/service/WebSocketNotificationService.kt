package com.tpass.mfa.service

import com.tpass.mfa.model.Challenge
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service

@Service
class WebSocketNotificationService(
    private val messagingTemplate: SimpMessagingTemplate
) {
    
    fun sendChallengeNotification(userId: String, challenge: Challenge) {
        val notification = mapOf(
            "type" to "NEW_CHALLENGE",
            "challengeId" to challenge.id,
            "createdAt" to challenge.createdAt,
            "expiresAt" to challenge.expiresAt,
            "nasIp" to challenge.nasIp,
            "callerId" to challenge.callerId
        )
        
        messagingTemplate.convertAndSend("/topic/challenges/$userId", notification)
        println("WebSocket notification sent to user: $userId, challenge: ${challenge.id}")
    }
    
    fun sendChallengeStatusUpdate(userId: String, challengeId: String, status: String) {
        val update = mapOf(
            "type" to "CHALLENGE_STATUS_UPDATE",
            "challengeId" to challengeId,
            "status" to status,
            "timestamp" to System.currentTimeMillis()
        )
        
        messagingTemplate.convertAndSend("/topic/challenges/$userId", update)
        println("WebSocket status update sent to user: $userId, challenge: $challengeId, status: $status")
    }
}

