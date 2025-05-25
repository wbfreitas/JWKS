package dev.wfreitas.jwks.infrastructure.dataproviders.token

import dev.wfreitas.jwks.domain.entity.RsaKey

interface RsaKeyProvider {
    fun getSigningKey(): RsaKey
    fun getAllPublicKeys(): List<RsaKey>
}