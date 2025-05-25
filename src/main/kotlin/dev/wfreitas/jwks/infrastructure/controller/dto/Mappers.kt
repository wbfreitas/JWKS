package dev.wfreitas.jwks.infrastructure.controller.dto

import dev.wfreitas.jwks.domain.entity.TokenInfo


fun TokenRequest.toDomain() = TokenInfo(
    idTransacional = this.idTransacional,
    codigoProduto = this.codigoProduto
)