package dev.wfreitas.jwks.application.usecase

import dev.wfreitas.jwks.domain.gateway.TokenVerifierGateway

class ValidateTokenUseCase (
    private val tokenVerifierGateway: TokenVerifierGateway
) {
    fun execute(token: String): Boolean {
        return tokenVerifierGateway.isValid(token)
    }
}