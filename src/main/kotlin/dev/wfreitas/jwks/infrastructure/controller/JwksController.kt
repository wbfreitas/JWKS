package dev.wfreitas.jwks.infrastructure.controller

import dev.wfreitas.jwks.infrastructure.controller.dto.JwkResponse
import dev.wfreitas.jwks.infrastructure.controller.dto.JwkSetResponse
import dev.wfreitas.jwks.infrastructure.dataproviders.token.RsaKeyProvider
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.math.BigInteger
import java.util.*

@RestController
class JwksController(
    private val keyProvider: RsaKeyProvider
) {

    @GetMapping("/.well-known/jwks.json")
    fun jwks(): JwkSetResponse {
        val keys = keyProvider.getAllPublicKeys().map { rsaKey ->
            JwkResponse(
                kty = "RSA",
                use = "sig",
                alg = "RS256",
                kid = rsaKey.kid,
                n = rsaKey.publicKey.modulus.toBase64Url(),
                e = rsaKey.publicKey.publicExponent.toBase64Url()
            )
        }
        return JwkSetResponse(keys)
    }

    private fun BigInteger.toBase64Url(): String {
        val bytes = this.toByteArray().dropWhile { it == 0.toByte() }.toByteArray()
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)
    }
}