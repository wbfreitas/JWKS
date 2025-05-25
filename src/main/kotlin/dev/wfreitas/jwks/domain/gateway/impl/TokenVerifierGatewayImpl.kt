package dev.wfreitas.jwks.domain.gateway.impl

import com.nimbusds.jose.crypto.RSASSAVerifier
import com.nimbusds.jwt.SignedJWT
import dev.wfreitas.jwks.domain.gateway.TokenVerifierGateway


import dev.wfreitas.jwks.infrastructure.dataproviders.token.RsaKeyProvider
import org.springframework.stereotype.Component
import java.util.*

@Component
class TokenVerifierGatewayImpl(
    private val keyProvider: RsaKeyProvider
) : TokenVerifierGateway {

    override fun isValid(token: String): Boolean {
        return try {
            val signedJWT = SignedJWT.parse(token)
            val rsaKey = keyProvider.getPublicKeyByKid(signedJWT.header.keyID)
                ?: throw IllegalArgumentException("Invalid token: no public key found for kid ${signedJWT.header.keyID}")

            val publicKey = rsaKey.publicKey

            signedJWT.verify(RSASSAVerifier(publicKey)) &&
                    !signedJWT.jwtClaimsSet.expirationTime.before(Date())
        } catch (e: Exception) {
            false
        }
    }
}
