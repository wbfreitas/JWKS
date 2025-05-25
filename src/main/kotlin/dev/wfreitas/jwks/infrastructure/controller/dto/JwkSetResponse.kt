package dev.wfreitas.jwks.infrastructure.controller.dto

data class JwkSetResponse(
    val keys: List<JwkResponse>
)

data class JwkResponse(
    val kty: String,
    val use: String,
    val alg: String,
    val kid: String,
    val n: String,
    val e: String
)