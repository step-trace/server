package com.steptrace.steptrace

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class StepTraceApplication

fun main(args: Array<String>) {
    runApplication<StepTraceApplication>(*args)
}
