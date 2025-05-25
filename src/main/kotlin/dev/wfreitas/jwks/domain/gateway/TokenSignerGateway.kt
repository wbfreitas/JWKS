package dev.wfreitas.jwks.domain.gateway

import dev.wfreitas.jwks.domain.entity.TokenInfo
import java.util.*

interface TokenSignerGateway {
    fun sign(tokenInfo: TokenInfo, expiration: Date): String
}