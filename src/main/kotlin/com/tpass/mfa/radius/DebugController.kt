package com.tpass.mfa.radius

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/debug")
class DebugController {
    
    @GetMapping("/test")
    fun test(): ResponseEntity<Map<String, String>> {
        println("=== DEBUG CONTROLLER TEST ===")
        return ResponseEntity.ok(mapOf("message" to "Debug controller works"))
    }
    
    @PostMapping("/test")
    fun testPost(@RequestBody body: Any): ResponseEntity<Map<String, Any>> {
        println("=== DEBUG CONTROLLER POST ===")
        println("Received body: $body")
        println("Body type: ${body::class.java}")
        
        return ResponseEntity.ok(mapOf(
            "message" to "Debug controller POST works",
            "received" to body.toString(),
            "type" to body::class.java.simpleName
        ))
    }
}
