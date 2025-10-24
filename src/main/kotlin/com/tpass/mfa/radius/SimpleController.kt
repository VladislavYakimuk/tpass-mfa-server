package com.tpass.mfa.radius

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/simple")
class SimpleController {
    
    @GetMapping("/test")
    fun test(): ResponseEntity<Map<String, String>> {
        return ResponseEntity.ok(mapOf("message" to "Hello from SimpleController"))
    }
    
    @PostMapping("/radius")
    fun testRadius(@RequestBody req: Map<String, Any>): ResponseEntity<Map<String, String>> {
        println("=== SIMPLE CONTROLLER ===")
        println("Received request: $req")
        
        val user = req["user"] as? String
        val nasIp = req["nas_ip"] as? String
        val caller = req["caller"] as? String
        val requestId = req["request_id"] as? String
        
        println("User: $user, NAS IP: $nasIp, Caller: $caller, Request ID: $requestId")
        
        if (user.isNullOrBlank()) {
            return ResponseEntity.badRequest().body(mapOf("result" to "reject", "reason" to "No user provided"))
        }
        
        // Простой тест - всегда возвращаем approve
        return ResponseEntity.ok(mapOf("result" to "approve", "reason" to "Simple test approved"))
    }
}
