package com.tpass.mfa.radius

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/minimal")
class MinimalController {
    
    @GetMapping("/test")
    fun test(): ResponseEntity<String> {
        return ResponseEntity.ok("Minimal controller works")
    }
    
    @PostMapping("/test")
    fun testPost(@RequestBody body: String): ResponseEntity<String> {
        return ResponseEntity.ok("Received: $body")
    }
}
