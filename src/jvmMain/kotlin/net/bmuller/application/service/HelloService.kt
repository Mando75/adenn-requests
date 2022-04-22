package net.bmuller.application.service

import net.bmuller.application.repository.HelloRepository

interface HelloService {
	fun sayHello(): String
}

class HelloServiceImpl(private val helloRepository: HelloRepository) : HelloService {
	override fun sayHello() = "Hello ${helloRepository.getHello()}!"
}