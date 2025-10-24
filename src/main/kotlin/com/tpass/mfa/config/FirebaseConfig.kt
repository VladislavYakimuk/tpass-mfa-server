package com.tpass.mfa.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.slf4j.LoggerFactory
import java.io.IOException

@Configuration
class FirebaseConfig {
    
    private val logger = LoggerFactory.getLogger(FirebaseConfig::class.java)
    
    init {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                // Используем firebase-adminsdk.json для правильной конфигурации
                val serviceAccount = ClassPathResource("firebase-adminsdk.json").inputStream
                val options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build()
                
                FirebaseApp.initializeApp(options)
                logger.info("Firebase initialized successfully with firebase-adminsdk.json")
            }
        } catch (e: Exception) {
            logger.error("Failed to initialize Firebase: ${e.message}", e)
            // Временное решение для тестирования без Firebase
            logger.warn("Firebase not initialized. Push notifications will not work.")
        }
    }
}
