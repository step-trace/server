package com.steptrace

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableFeignClients
@EnableJpaAuditing
@EnableScheduling
@ConfigurationPropertiesScan
class StepTraceApplication
fun main(args: Array<String>) {
    runApplication<StepTraceApplication>(*args)
}
