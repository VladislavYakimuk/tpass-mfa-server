package com.tpass.mfa.service

import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import org.springframework.stereotype.Service
import org.slf4j.LoggerFactory

@Service
class FcmService {
    
    private val logger = LoggerFactory.getLogger(FcmService::class.java)
    
    init {
        try {
            // Firebase Admin SDK уже инициализирован в FirebaseConfig
            logger.info("FcmService initialized")
        } catch (e: Exception) {
            logger.error("Failed to initialize FcmService", e)
        }
    }
    
    fun sendNotification(fcmToken: String, title: String, body: String, data: Map<String, String> = emptyMap()): Boolean {
        return try {
            logger.info("Sending FCM notification to token: $fcmToken")
            logger.info("Title: $title, Body: $body")
            logger.info("Data: $data")
            
            val message = Message.builder()
                .setToken(fcmToken)
                .setNotification(
                    Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build()
                )
                .putAllData(data)
                .build()
            
            val response = FirebaseMessaging.getInstance().send(message)
            logger.info("Successfully sent FCM message: $response")
            true
        } catch (e: Exception) {
            logger.error("Failed to send FCM message to token: $fcmToken", e)
            logger.error("Error details: ${e.message}")
            false
        }
    }
    
    fun sendMfaChallenge(fcmToken: String, challengeId: String, userId: String, nasIp: String?, callerId: String?): Boolean {
        val title = "TPass MFA Challenge"
        val body = "Подтвердите вход в систему для пользователя: $userId"
        
        val data = mapOf(
            "challengeId" to challengeId,
            "userId" to userId,
            "nasIp" to (nasIp ?: ""),
            "callerId" to (callerId ?: ""),
            "type" to "mfa_challenge"
        )
        
        return sendNotification(fcmToken, title, body, data)
    }
    
    fun sendChallengeNotification(
        fcmToken: String,
        challengeId: String,
        challengeData: String,
        nasIp: String?,
        callerId: String?
    ): Boolean {
        val title = "TPass MFA Challenge"
        val body = "Подтвердите вход в систему"
        
        val data = mapOf(
            "challengeId" to challengeId,
            "challengeData" to challengeData,
            "nasIp" to (nasIp ?: ""),
            "callerId" to (callerId ?: ""),
            "type" to "mfa_challenge"
        )
        
        return sendNotification(fcmToken, title, body, data)
    }
}