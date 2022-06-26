package com.example.kinesisaop

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties
class KinesisAopApplication

fun main(args: Array<String>) {
    runApplication<KinesisAopApplication>(*args)
}
