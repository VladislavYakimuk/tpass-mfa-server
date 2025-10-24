package com.tpass.mfa.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "devices")
data class Device(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    
    @Column(unique = true, nullable = false)
    val deviceId: String,
    
    @Column(nullable = false)
    val userId: String,
    
    @Column(nullable = false)
    val deviceName: String,
    
    @Column(nullable = false)
    val publicKey: String,
    
    @Column(nullable = false)
    val fcmToken: String? = null,
    
    @Column(nullable = false)
    val isActive: Boolean = true,
    
    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),
    
    @Column
    val lastSeenAt: LocalDateTime? = null
)
