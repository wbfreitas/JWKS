package dev.wfreitas.jwks.domain.gateway.impl

import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.crypto.RSASSASigner
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import dev.wfreitas.jwks.domain.entity.TokenInfo
import dev.wfreitas.jwks.domain.gateway.TokenSignerGateway
import dev.wfreitas.jwks.infrastructure.dataproviders.token.RsaKeyProvider
import org.springframework.stereotype.Component

import java.util.*

@Component
class TokenSignerGatewayImpl(
    private val keyProvider: RsaKeyProvider
) : TokenSignerGateway {

    override fun sign(tokenInfo: TokenInfo, expiration: Date): String {
        val signingKey = keyProvider.getSigningKey()

        val claims = JWTClaimsSet.Builder()
            .issueTime(Date())
            .expirationTime(expiration)
            .claim("idTransacional", tokenInfo.idTransacional)
            .claim("codigoProduto", tokenInfo.codigoProduto)
            .build()

        val signedJWT = SignedJWT(
            JWSHeader.Builder(JWSAlgorithm.RS256)
                .keyID(signingKey.kid)
                .build(),
            claims
        )

        signedJWT.sign(RSASSASigner(signingKey.privateKey))

        return signedJWT.serialize()
    }
}