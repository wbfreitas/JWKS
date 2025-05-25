package dev.wfreitas.jwks.infrastructure.controller.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size


data class TokenRequest(
    @field:NotBlank(message = "idTransacional é obrigatório")
    val idTransacional: String,

    @field:Size(min = 1, message = "codigoProduto deve conter pelo menos um item")
    val codigoProduto: List<@NotBlank(message = "codigoProduto não pode conter strings em branco") String>
)