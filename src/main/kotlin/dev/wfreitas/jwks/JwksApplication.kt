package dev.wfreitas.jwks

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class JwksApplication

fun main(args: Array<String>) {
	runApplication<JwksApplication>(*args)
}
