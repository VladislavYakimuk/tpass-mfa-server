package com.tpass.mfa.controller

import com.tpass.mfa.service.FcmService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/test")
class TestFcmController(
    private val fcmService: FcmService
) {
    
    @PostMapping("/fcm")
    fun testFcm(@RequestParam fcmToken: String): ResponseEntity<Map<String, Any>> {
        return try {
            val success = fcmService.sendNotification(
                fcmToken = fcmToken,
                title = "Test FCM",
                body = "This is a test FCM notification",
                data = mapOf("test" to "true")
            )
            
            if (success) {
                ResponseEntity.ok(mapOf("success" to true, "message" to "FCM notification sent"))
            } else {
                ResponseEntity.ok(mapOf("success" to false, "message" to "Failed to send FCM notification"))
            }
        } catch (e: Exception) {
            ResponseEntity.ok(mapOf("success" to false, "message" to "Error: ${e.message}"))
        }
    }
}
