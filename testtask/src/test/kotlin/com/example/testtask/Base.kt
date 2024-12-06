package com.example.testtask

import com.example.testtask.dto.ToDoDTO
import com.example.testtask.service.ToDoService
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.qameta.allure.Allure.step
import io.qameta.allure.Feature
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.TestInfo
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_METHOD
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.http.HttpStatus


@SpringBootApplication
@ComponentScan(basePackages = ["com.example.testtask"])
@EnableAutoConfiguration(exclude = [DataSourceAutoConfiguration::class])
@SpringBootTest
@Suppress("UNCHECKED_CAST")
@Feature("todo-list-service")
@TestInstance(PER_METHOD)
class Base {

    @Autowired
    lateinit var baseService: ToDoService

    val objectMapper = jacksonObjectMapper()

    var createdTodoId: Array<ToDoDTO>? = null

    @AfterEach
    fun cleanTasks(testInfo: TestInfo){
        if (testInfo.tags.contains("skipCleanup")) {
            println("Skipping cleanup")
            return
        }
        step("Delete task")
        val deleteTask = baseService.deleteTasks(createdTodoId!!.first().id!!)
        assertThat(deleteTask.statusCode).isEqualTo(HttpStatus.NO_CONTENT)

        step("Check delete")
        assertThat(baseService.getTasks().body as Array<ToDoDTO>).isEmpty()
    }
}