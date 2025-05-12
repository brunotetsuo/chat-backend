package com.uai.aquecer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AquecerApplication

fun main(args: Array<String>) {
	runApplication<AquecerApplication>(*args)
}
