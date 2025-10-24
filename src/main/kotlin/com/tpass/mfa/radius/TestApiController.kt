package com.tpass.mfa.radius

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/test-api")
class TestApiController {
    
    @GetMapping("/ping")
    fun ping(): ResponseEntity<Map<String, String>> {
        println("=== PING REQUEST ===")
        return ResponseEntity.ok(mapOf("message" to "API is working", "timestamp" to System.currentTimeMillis().toString()))
    }
    
    @PostMapping("/radius")
    fun testRadius(@RequestBody body: Map<String, Any>): ResponseEntity<Map<String, Any>> {
        println("=== TEST RADIUS REQUEST ===")
        println("Received body: $body")
        
        val user = body["user"] as? String
        val nasIp = body["nas_ip"] as? String
        val caller = body["caller"] as? String
        val requestId = body["request_id"] as? String
        
        println("User: $user, NAS IP: $nasIp, Caller: $caller, Request ID: $requestId")
        
        if (user.isNullOrBlank()) {
            return ResponseEntity.badRequest().body(mapOf("result" to "reject", "reason" to "No user provided"))
        }
        
        return ResponseEntity.ok(mapOf(
            "result" to "approve",
            "reason" to "Test approval",
            "challengeId" to "test-challenge-${System.currentTimeMillis()}",
            "user" to (user ?: ""),
            "nasIp" to (nasIp ?: ""),
            "caller" to (caller ?: ""),
            "requestId" to (requestId ?: "")
        ))
    }
}
