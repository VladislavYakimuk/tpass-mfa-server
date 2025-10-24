package com.tpass.mfa.repository

import com.tpass.mfa.model.Challenge
import com.tpass.mfa.model.ChallengeStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*

@Repository
interface ChallengeRepository : JpaRepository<Challenge, Long> {
    fun findByChallengeId(challengeId: String): Optional<Challenge>
    fun findByUserIdAndStatus(userId: String, status: ChallengeStatus): List<Challenge>
    fun findByDeviceIdAndStatus(deviceId: String, status: ChallengeStatus): List<Challenge>
    fun findByStatusAndExpiresAtBefore(status: ChallengeStatus, expiresAt: LocalDateTime): List<Challenge>
}
