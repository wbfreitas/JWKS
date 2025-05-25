package dev.wfreitas.jwks.application.usecase

import dev.wfreitas.jwks.domain.entity.TokenInfo
import dev.wfreitas.jwks.domain.gateway.TokenSignerGateway
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GenerateTokenUseCaseTest {

    private val signer = mock<TokenSignerGateway>()
    private val expirationDays = 30L
    private val useCase = GenerateTokenUseCase(signer, expirationDays)

    @Test
    fun `should generate token with expiration date and delegate to signer`() {
        val tokenInfo = TokenInfo("abc123", listOf("PROD1", "PROD2"))
        val expectedToken = "token-assinado"
        val now = System.currentTimeMillis()

        whenever(signer.sign(any(), any())).thenReturn(expectedToken)

        val token = useCase.execute(tokenInfo)

        assertEquals(expectedToken, token)

        argumentCaptor<Date>().apply {
            verify(signer).sign(eq(tokenInfo), capture())
            val expiration = firstValue.time
            val expectedMin = now + expirationDays * 24 * 60 * 60 * 1000
            assertTrue(expiration in expectedMin..(expectedMin + 5000), "Data de expiração fora do intervalo")
        }
    }
}