package com.application

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@EntityScan(basePackages = ["com"])
@ComponentScan(basePackages = ["com"])
@EnableJpaRepositories(basePackages = ["com"])
@SpringBootApplication
class SingleDeviceLoginApplication

fun main(args: Array<String>) {
    runApplication<SingleDeviceLoginApplication>(*args)
}