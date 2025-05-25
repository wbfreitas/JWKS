package dev.wfreitas.jwks.infrastructure.controller

import dev.wfreitas.jwks.application.usecase.GenerateTokenUseCase
import dev.wfreitas.jwks.domain.entity.TokenInfo
import dev.wfreitas.jwks.infrastructure.controller.dto.TokenRequest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.whenever
import org.springframework.http.ResponseEntity

class TokenControllerTest {

    private val useCase = mock<GenerateTokenUseCase>()
    private val controller = TokenController(useCase)

    @Test
    fun `should generate token successfully`() {
        val request = TokenRequest(
            idTransacional = "abc123",
            codigoProduto = listOf("PROD1", "PROD2")
        )
        val expectedToken = "fake.jwt.token"

        whenever(useCase.execute(any())).thenReturn(expectedToken)

        val response: ResponseEntity<String> = controller.generate(request)

        assertEquals(200, response.statusCode.value())
        assertEquals(expectedToken, response.body)

        argumentCaptor<TokenInfo>().apply {
            verify(useCase).execute(capture())
            assertEquals("abc123", firstValue.idTransacional)
            assertEquals(listOf("PROD1", "PROD2"), firstValue.codigoProduto)
        }
    }
}