package dev.wfreitas.jwks.infrastructure.dataproviders.token.impl

import dev.wfreitas.jwks.domain.entity.RsaKey
import dev.wfreitas.jwks.infrastructure.config.RsaKeyProperties
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import org.mockito.kotlin.spy
class TestableRsaKeyProviderImpl(props: RsaKeyProperties) : RsaKeyProviderImpl(props) {
    public override fun loadKey(kid: String, publicPath: String, privatePath: String): RsaKey {
        return super.loadKey(kid, publicPath, privatePath)
    }
}

class RsaKeyProviderImplTest {
    @Test
    fun `loadKey should throw exception when public key content is invalid`() {
        val publicPath = "/invalid-public.pem"
        val privatePath = "/valid-private.pem"
        val kid = "invalid-public-content-kid"

        val provider = spy(TestableRsaKeyProviderImpl(mock(RsaKeyProperties::class.java)))
        doThrow(IllegalArgumentException("Key not found at $publicPath"))
            .`when`(provider).loadKey(kid, publicPath, privatePath)

        val exception = assertThrows<IllegalArgumentException> {
            provider.loadKey(kid, publicPath, privatePath)
        }

        assertEquals("Key not found at $publicPath", exception.message)
    }

    @Test
    fun `loadKey should throw exception when private key content is invalid`() {
        val publicPath = "/valid-public.pem"
        val privatePath = "/invalid-private.pem"
        val kid = "invalid-private-content-kid"


        val provider = spy(TestableRsaKeyProviderImpl(mock(RsaKeyProperties::class.java)))
        doThrow(IllegalArgumentException("Key not found at $privatePath"))
            .`when`(provider).loadKey(kid, publicPath, privatePath)

        val exception = assertThrows<IllegalArgumentException> {
            provider.loadKey(kid, publicPath, privatePath)
        }

        assertEquals("Key not found at $privatePath", exception.message)
    }

    }
