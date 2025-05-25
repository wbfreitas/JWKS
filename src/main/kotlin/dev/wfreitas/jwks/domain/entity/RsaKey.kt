package dev.wfreitas.jwks.domain.entity

import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey

data class RsaKey(
    val kid: String,
    val publicKey: RSAPublicKey,
    val privateKey: RSAPrivateKey
)