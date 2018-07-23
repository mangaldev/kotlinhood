package com.stock.kotlinhood

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KotlinhoodApplication

fun main(args: Array<String>) {
    runApplication<KotlinhoodApplication>(*args)
}

private val log = LoggerFactory.getLogger(KotlinhoodApplication::class.java)


