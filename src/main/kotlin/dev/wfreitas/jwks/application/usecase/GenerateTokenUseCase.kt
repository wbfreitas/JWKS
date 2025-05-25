package dev.wfreitas.jwks.application.usecase

import dev.wfreitas.jwks.domain.entity.TokenInfo
import dev.wfreitas.jwks.domain.gateway.TokenSignerGateway
import java.util.*

class GenerateTokenUseCase (
    private val tokenSignerGateway: TokenSignerGateway,
    private val expirationInDays: Long
) {
    fun execute(tokenInfo: TokenInfo): String {
        val expirationDate = Date(System.currentTimeMillis() + expirationInDays * 24 * 60 * 60 * 1000)
        return tokenSignerGateway.sign(tokenInfo, expirationDate)
    }
}