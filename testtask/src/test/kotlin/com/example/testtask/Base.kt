package com.example.testtask

import com.example.testtask.service.ToDoService
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.qameta.allure.Feature
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan


@SpringBootApplication
@ComponentScan(basePackages = ["com.example.testtask"])
@EnableAutoConfiguration(exclude = [DataSourceAutoConfiguration::class])
@SpringBootTest
@Feature("todo-list-service")
@TestInstance(PER_CLASS)
class Base {
    //url's
    @Value("\${base-url.todoservice}")
    lateinit var baseUrl: String

    @Autowired
    lateinit var baseService: ToDoService

    val objectMapper = jacksonObjectMapper()
}