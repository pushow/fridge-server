package com.fridge.fridge_server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FridgeServerApplication

fun main(args: Array<String>) {
	runApplication<FridgeServerApplication>(*args)
}
