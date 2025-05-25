package dev.wfreitas.jwks.domain.gateway

interface TokenVerifierGateway {
    fun isValid(token: String): Boolean
}