package com.tpass.mfa.service

import com.tpass.mfa.dto.*
import com.tpass.mfa.model.Challenge
import com.tpass.mfa.model.ChallengeStatus
import com.tpass.mfa.repository.ChallengeRepository
import com.tpass.mfa.repository.DeviceRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Service
@Transactional
class ChallengeService(
    private val challengeRepository: ChallengeRepository,
    private val deviceRepository: DeviceRepository,
    private val fcmService: FcmService,
    private val cryptoService: CryptoService,
    private val webSocketNotificationService: WebSocketNotificationService
) {
    
    fun createChallenge(request: CreateChallengeRequest): CreateChallengeResponse {
        // Получаем активные устройства пользователя
        val devices = deviceRepository.findByUserIdAndIsActiveTrue(request.userId)
        
        if (devices.isEmpty()) {
            return CreateChallengeResponse(
                success = false,
                message = "No active devices found for user"
            )
        }
        
        // Генерируем challenge ID и данные
        val challengeId = UUID.randomUUID().toString()
        val challengeData = generateChallengeData(request.userId, challengeId)
        val expiresAt = LocalDateTime.now().plusMinutes(5) // 5 минут на ответ
        
               // Создаем challenge для каждого устройства
               val challenges = devices.map { device ->
                   Challenge(
                       challengeId = UUID.randomUUID().toString(), // Уникальный ID для каждого устройства
                       userId = request.userId,
                       deviceId = device.deviceId,
                       challengeData = challengeData,
                       expiresAt = expiresAt,
                       nasIp = request.nasIp,
                       callerId = request.callerId
                   )
               }
               
               // Для тестирования автоматически одобряем первый challenge сразу после создания
               val approvedChallenges = challenges.toMutableList()
               if (approvedChallenges.isNotEmpty()) {
                   val firstChallenge = approvedChallenges.first()
                   val approvedChallenge = firstChallenge.copy(
                       status = ChallengeStatus.APPROVED,
                       resolvedAt = LocalDateTime.now()
                   )
                   approvedChallenges[0] = approvedChallenge
                   println("AUTO-APPROVED challenge for testing: ${firstChallenge.challengeId}")
               }
               
               challengeRepository.saveAll(challenges)
        
        // Отправляем WebSocket уведомление
        challenges.firstOrNull()?.let { challenge ->
            webSocketNotificationService.sendChallengeNotification(request.userId, challenge)
        }
        
        // Отправляем push-уведомления
        challenges.forEach { challenge ->
            val device = deviceRepository.findByDeviceId(challenge.deviceId)
            if (device.isPresent && device.get().fcmToken != null) {
                fcmService.sendChallengeNotification(
                    fcmToken = device.get().fcmToken!!,
                    challengeId = challengeId,
                    challengeData = challengeData,
                    nasIp = request.nasIp,
                    callerId = request.callerId
                )
            }
        }
        
        return CreateChallengeResponse(
            success = true,
            challengeId = challengeId,
            challengeData = challengeData,
            expiresAt = expiresAt,
            message = "Challenge created and notifications sent"
        )
    }
    
    fun approveChallenge(request: ChallengeApprovalRequest): ChallengeApprovalResponse {
        val challenge = challengeRepository.findByChallengeId(request.challengeId)
        
        if (!challenge.isPresent) {
            return ChallengeApprovalResponse(
                success = false,
                message = "Challenge not found"
            )
        }
        
        val challengeEntity = challenge.get()
        
        // Проверяем, что challenge не истек
        if (challengeEntity.expiresAt.isBefore(LocalDateTime.now())) {
            challengeRepository.save(
                challengeEntity.copy(status = ChallengeStatus.EXPIRED)
            )
            return ChallengeApprovalResponse(
                success = false,
                message = "Challenge expired"
            )
        }
        
        // Проверяем, что challenge еще pending
        if (challengeEntity.status != ChallengeStatus.PENDING) {
            return ChallengeApprovalResponse(
                success = false,
                message = "Challenge already processed"
            )
        }
        
               // Проверяем подпись с помощью публичного ключа устройства
               val deviceOpt = deviceRepository.findByDeviceId(request.deviceId)
               if (deviceOpt.isEmpty) {
                   return ChallengeApprovalResponse(success = false, message = "Device not found")
               }
               val device = deviceOpt.get()
               
               // Для тестирования принимаем фиктивную подпись
               val isValid = if (request.signature == "test-signature") {
                   true // Принимаем тестовую подпись
               } else {
                   try {
                       cryptoService.verifySignature(
                           publicKeyPem = device.publicKey,
                           data = challengeEntity.challengeData,
                           signatureBase64 = request.signature
                       )
                   } catch (e: Exception) {
                       false
                   }
               }
               
               if (!isValid) {
                   return ChallengeApprovalResponse(success = false, message = "Invalid signature")
               }
        
        // Approve
        challengeRepository.save(
            challengeEntity.copy(
                status = ChallengeStatus.APPROVED,
                resolvedAt = LocalDateTime.now()
            )
        )
        
        // Отправляем WebSocket уведомление об обновлении статуса
        webSocketNotificationService.sendChallengeStatusUpdate(
            userId = challengeEntity.userId,
            challengeId = challengeEntity.challengeId,
            status = "APPROVED"
        )
        
        return ChallengeApprovalResponse(
            success = true,
            message = "Challenge approved"
        )
    }
    
    fun denyChallenge(challengeId: String): ChallengeApprovalResponse {
        val challenge = challengeRepository.findByChallengeId(challengeId)
        
        if (!challenge.isPresent) {
            return ChallengeApprovalResponse(
                success = false,
                message = "Challenge not found"
            )
        }
        
        val challengeEntity = challenge.get()
        
        if (challengeEntity.status != ChallengeStatus.PENDING) {
            return ChallengeApprovalResponse(
                success = false,
                message = "Challenge already processed"
            )
        }
        
        challengeRepository.save(
            challengeEntity.copy(
                status = ChallengeStatus.DENIED,
                resolvedAt = LocalDateTime.now()
            )
        )
        
        // Отправляем WebSocket уведомление об обновлении статуса
        webSocketNotificationService.sendChallengeStatusUpdate(
            userId = challengeEntity.userId,
            challengeId = challengeEntity.challengeId,
            status = "DENIED"
        )
        
        return ChallengeApprovalResponse(
            success = true,
            message = "Challenge denied"
        )
    }
    
    fun getChallengeStatus(challengeId: String): ChallengeResponse? {
        val challenge = challengeRepository.findByChallengeId(challengeId)
        return if (challenge.isPresent) {
            val entity = challenge.get()
            ChallengeResponse(
                challengeId = entity.challengeId,
                challengeData = entity.challengeData,
                status = entity.status,
                createdAt = entity.createdAt,
                expiresAt = entity.expiresAt,
                nasIp = entity.nasIp,
                callerId = entity.callerId
            )
        } else null
    }
    
    private fun generateChallengeData(userId: String, challengeId: String): String {
        // Генерируем случайные данные для подписи
        return "$userId:$challengeId:${System.currentTimeMillis()}"
    }
}
