package dev.wfreitas.jwks.infrastructure.controller

import dev.wfreitas.jwks.application.usecase.GenerateTokenUseCase

import dev.wfreitas.jwks.infrastructure.controller.dto.TokenRequest
import dev.wfreitas.jwks.infrastructure.controller.dto.toDomain
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/token")
class TokenController(
    private val generateTokenUseCase: GenerateTokenUseCase
) {

    @PostMapping("/generate")
    fun generate(@RequestBody request: TokenRequest): ResponseEntity<String> {
        val token = generateTokenUseCase.execute(request.toDomain())
        return ResponseEntity.ok(token)
    }
}