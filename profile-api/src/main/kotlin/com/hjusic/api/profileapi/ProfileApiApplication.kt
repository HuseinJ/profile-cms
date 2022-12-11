package com.hjusic.api.profileapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
class ProfileApiApplication

fun main(args: Array<String>) {
    runApplication<ProfileApiApplication>(*args)
}
