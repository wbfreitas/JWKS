package dev.wfreitas.jwks.infrastructure.controller

import org.junit.jupiter.api.Assertions.*

import dev.wfreitas.jwks.domain.entity.RsaKey
import dev.wfreitas.jwks.infrastructure.dataproviders.token.RsaKeyProvider

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.math.BigInteger
import java.security.KeyFactory
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.RSAPrivateKeySpec
import java.security.spec.RSAPublicKeySpec
import java.util.*

class JwksControllerTest {

    private val mockKeyProvider = mock<RsaKeyProvider>()
    private val controller = JwksController(mockKeyProvider)

    @Test
    fun `should return valid JWKS structure`() {
        val kid = "test-kid"
        val publicKey = generateFakePublicKey()
        val privateKey = generateFakePrivateKey()
        val rsaKey = RsaKey(kid, publicKey, privateKey)

        whenever(mockKeyProvider.getAllPublicKeys()).thenReturn(listOf(rsaKey))

        val response = controller.jwks()

        assertEquals(1, response.keys.size)
        val key = response.keys.first()
        assertEquals("RSA", key.kty)
        assertEquals("RS256", key.alg)
        assertEquals("sig", key.use)
        assertEquals(kid, key.kid)
        assertNotNull(key.n)
        assertNotNull(key.e)
    }

    private fun generateFakePublicKey(): RSAPublicKey {
        val modulus = BigInteger("00af7954c1f9b9e5d3a2b7c8d9e0f123456789abcdef123456789abcdef123456789abcdef123456789abcdef123456789abcdef123456789abcdef123456789abcdef123456789abcdef123456789abcdef123456789abcdef123456789abcdef123456789abcdef123456789abcdef123456789abcdef123456789abcdef123456789abcdef123456789abcdef123456789abcdef", 16)
        val exponent = BigInteger("010001", 16) // 65537
        val spec = RSAPublicKeySpec(modulus, exponent)
        return KeyFactory.getInstance("RSA").generatePublic(spec) as RSAPublicKey
    }

    private fun generateFakePrivateKey(): RSAPrivateKey {
        val modulus = BigInteger("00af7954c1f9b9e5d3a2b7c8d9e0f123456789abcdef123456789abcdef123456789abcdef123456789abcdef123456789abcdef123456789abcdef123456789abcdef123456789abcdef123456789abcdef123456789abcdef123456789abcdef123456789abcdef123456789abcdef123456789abcdef123456789abcdef123456789abcdef", 16)
        val privateExponent = BigInteger("5b5b5b5b5b5b5b5b5b5b5b5b5b5b5b5b5b5b5b5b5b5b5b5b5b5b5b5b5b5b5b5b5b5b5b5b5b5b5b5b5b5b5b5b5b5b5b5b5b5b5b5b5b5b5b5b5b5b5b5b5b5b5b5b5b5b5b5b5b5b5b5b5b5b5b5b5b5b5b", 16)
        val spec = RSAPrivateKeySpec(modulus, privateExponent)
        return KeyFactory.getInstance("RSA").generatePrivate(spec) as RSAPrivateKey
    }
}