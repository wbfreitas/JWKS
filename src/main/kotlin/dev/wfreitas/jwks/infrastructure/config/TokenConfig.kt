package dev.wfreitas.jwks.infrastructure.config

import dev.wfreitas.jwks.application.usecase.GenerateTokenUseCase

import dev.wfreitas.jwks.domain.gateway.TokenSignerGateway
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TokenConfig {

    @Bean
    fun generateTokenUseCase(
        tokenSignerGateway: TokenSignerGateway,
        @Value("\${token.expiration.days:30}") expirationDays: Long
    ): GenerateTokenUseCase {
        return GenerateTokenUseCase(tokenSignerGateway, expirationDays)
    }
}