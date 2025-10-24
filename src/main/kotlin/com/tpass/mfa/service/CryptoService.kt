package com.tpass.mfa.service

import org.springframework.stereotype.Service
import java.security.KeyFactory
import java.security.Signature
import java.security.spec.X509EncodedKeySpec
import java.util.Base64

@Service
class CryptoService {
	fun verifySignature(publicKeyPem: String, data: String, signatureBase64: String): Boolean {
		val publicKey = parsePublicKey(publicKeyPem)
		val sig = Signature.getInstance("SHA256withRSA")
		sig.initVerify(publicKey)
		sig.update(data.toByteArray(Charsets.UTF_8))
		val sigBytes = Base64.getDecoder().decode(signatureBase64)
		return sig.verify(sigBytes)
	}

	private fun parsePublicKey(pem: String): java.security.PublicKey {
		val clean = pem
			.replace("-----BEGIN PUBLIC KEY-----", "")
			.replace("-----END PUBLIC KEY-----", "")
			.replace("\n", "")
			.replace("\r", "")
		val decoded = Base64.getDecoder().decode(clean)
		val keySpec = X509EncodedKeySpec(decoded)
		val kf = KeyFactory.getInstance("RSA")
		return kf.generatePublic(keySpec)
	}
}
