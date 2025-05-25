package dev.wfreitas.jwks.infrastructure.controller.dto

import jakarta.validation.constraints.NotBlank

data class TokenValidationRequest(
    @field:NotBlank(message = "token é obrigatório")
    val token: String
)