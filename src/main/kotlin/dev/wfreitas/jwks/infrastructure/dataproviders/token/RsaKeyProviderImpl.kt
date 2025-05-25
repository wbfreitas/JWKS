package dev.wfreitas.jwks.infrastructure.dataproviders.token

import dev.wfreitas.jwks.domain.entity.RsaKey
import dev.wfreitas.jwks.infrastructure.config.RsaKeyProperties
import org.springframework.stereotype.Component
import java.io.InputStream
import java.security.KeyFactory
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*

@Component
class RsaKeyProviderImpl(
    private val props: RsaKeyProperties
) : RsaKeyProvider {

    private val keys: Map<String, RsaKey> by lazy {
        props.list.associate { entry ->
            val key = loadKey(entry.kid, entry.publicPath, entry.privatePath)
            entry.kid to key
        }
    }

    private val signingKid = props.signingKid

    override fun getSigningKey(): RsaKey {
        return keys[signingKid]
            ?: throw IllegalStateException("Signing key '$signingKid' not found.")
    }

    override fun getPublicKeyByKid(kid: String): RsaKey? {
        return keys[kid]
    }

    override fun getAllPublicKeys(): List<RsaKey> {
        return keys.values.toList()
    }

    private fun loadKey(kid: String, publicPath: String, privatePath: String): RsaKey {
        val publicKey = readPublicKey(publicPath)
        val privateKey = readPrivateKey(privatePath)
        return RsaKey(kid = kid, publicKey = publicKey, privateKey = privateKey)
    }

    private fun readPemContent(path: String): ByteArray {
        val stream: InputStream = javaClass.getResourceAsStream(path)
            ?: throw IllegalArgumentException("Key not found at $path")
        val pem = stream.bufferedReader().use { it.readText() }
        val base64 = pem
            .replace(Regex("-----BEGIN (.*?)-----|-----END (.*?)-----"), "")
            .replace("\\s+".toRegex(), "")
        return Base64.getDecoder().decode(base64)
    }

    private fun generatePublicKeySpec(path: String): X509EncodedKeySpec {
        val keyBytes = readPemContent(path)
        return X509EncodedKeySpec(keyBytes)
    }

    private fun readPublicKey(path: String): RSAPublicKey {
        val spec = generatePublicKeySpec(path)
        return KeyFactory.getInstance("RSA").generatePublic(spec) as RSAPublicKey
    }

    private fun readPrivateKey(path: String): RSAPrivateKey {
        val keyBytes = readPemContent(path)
        val spec = PKCS8EncodedKeySpec(keyBytes)
        return KeyFactory.getInstance("RSA").generatePrivate(spec) as RSAPrivateKey
    }
}
