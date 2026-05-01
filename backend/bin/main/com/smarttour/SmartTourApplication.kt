package com.smarttour

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SmartTourApplication

fun main(args: Array<String>) {
    runApplication<SmartTourApplication>(*args)
}
