package dev.wfreitas.jwks.infrastructure.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "keys")
class RsaKeyProperties {
    lateinit var signingKid: String
    var list: List<RsaKeyEntry> = listOf()

    class RsaKeyEntry {
        lateinit var kid: String
        lateinit var publicPath: String
        lateinit var privatePath: String
    }
}