package com.tpass.mfa.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "challenges")
data class Challenge(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    
    @Column(unique = true, nullable = false)
    val challengeId: String,
    
    @Column(nullable = false)
    val userId: String,
    
    @Column(nullable = false)
    val deviceId: String,
    
    @Column(nullable = false)
    val challengeData: String,
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val status: ChallengeStatus = ChallengeStatus.PENDING,
    
    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),
    
    @Column
    val expiresAt: LocalDateTime,
    
    @Column
    val resolvedAt: LocalDateTime? = null,
    
    @Column
    val nasIp: String? = null,
    
    @Column
    val callerId: String? = null
)

enum class ChallengeStatus {
    PENDING,
    APPROVED,
    DENIED,
    EXPIRED
}
