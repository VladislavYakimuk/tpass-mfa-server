package com.tpass.mfa.controller

import com.tpass.mfa.dto.*
import com.tpass.mfa.service.ChallengeService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/challenges")
class ChallengeController(
    private val challengeService: ChallengeService
) {
    
    @PostMapping("/create")
    fun createChallenge(@Valid @RequestBody request: CreateChallengeRequest): ResponseEntity<CreateChallengeResponse> {
        val response = challengeService.createChallenge(request)
        return if (response.success) {
            ResponseEntity.ok(response)
        } else {
            ResponseEntity.badRequest().body(response)
        }
    }
    
    @PostMapping("/approve")
    fun approveChallenge(@Valid @RequestBody request: ChallengeApprovalRequest): ResponseEntity<ChallengeApprovalResponse> {
        val response = challengeService.approveChallenge(request)
        return if (response.success) {
            ResponseEntity.ok(response)
        } else {
            ResponseEntity.badRequest().body(response)
        }
    }
    
    @PostMapping("/{challengeId}/deny")
    fun denyChallenge(@PathVariable challengeId: String): ResponseEntity<ChallengeApprovalResponse> {
        val response = challengeService.denyChallenge(challengeId)
        return if (response.success) {
            ResponseEntity.ok(response)
        } else {
            ResponseEntity.badRequest().body(response)
        }
    }
    
    @GetMapping("/{challengeId}/status")
    fun getChallengeStatus(@PathVariable challengeId: String): ResponseEntity<ChallengeResponse> {
        val response = challengeService.getChallengeStatus(challengeId)
        return if (response != null) {
            ResponseEntity.ok(response)
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
