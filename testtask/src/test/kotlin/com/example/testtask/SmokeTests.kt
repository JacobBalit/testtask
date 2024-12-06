package com.example.testtask

import com.example.testtask.dto.ToDoDTO
import com.example.testtask.testdata.TestData.createTaskRequest
import com.example.testtask.testdata.TestData.updateTaskRequest
import io.qameta.allure.Allure.step
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import kotlin.random.Random.Default.nextInt

@Suppress("UNCHECKED_CAST")
@DisplayName("Tests per request /todo")
class SmokeTests : Base() {

    @Test
    @DisplayName("Create task and check result")
    fun postList() {
        step("Create task")
        val responsePostTask = baseService.postTasks(createTaskRequest)
        assertThat(responsePostTask.statusCode).isEqualTo(HttpStatus.CREATED)

        step("Get task")
        val responseGetTask = baseService.getTasks()
        assertThat(responseGetTask.statusCode).isEqualTo(HttpStatus.OK)
        val responseBody = responseGetTask.body!! as Array<ToDoDTO>
        assertThat(responseBody.find { it.id == createTaskRequest.id }).isEqualTo(createTaskRequest)
        //delete task
        createdTodoId = responseBody
    }

    @Test
    @DisplayName("Update task and check result")
    fun updateTask() {
        step("Create task")
        val responsePostTask = baseService.postTasks(createTaskRequest)
        assertThat(responsePostTask.statusCode).isEqualTo(HttpStatus.CREATED)

        step("Update task")
        val responseUpdateTask = baseService.putTasks(createTaskRequest.id!!, updateTaskRequest)
        assertThat(responseUpdateTask.statusCode).isEqualTo(HttpStatus.OK)

        step("Get updated task")
        val responseGetTask = baseService.getTasks()
        assertThat(responseGetTask.statusCode).isEqualTo(HttpStatus.OK)
        val responseBody = responseGetTask.body!! as Array<ToDoDTO>
        assertThat(responseBody.find { it.id == updateTaskRequest.id }).isEqualTo(updateTaskRequest)

        //delete task
        createdTodoId = responseBody
    }

    @Test
    @DisplayName("Update task one param and check result")
    fun updateTaskOneField() {
        step("Create task")
        val request = ToDoDTO(
            id = nextInt(1, Int.MAX_VALUE),
            text = "TestTask" + nextInt(1, Int.MAX_VALUE),
            completed = false
        )
        val responsePostTask = baseService.postTasks(request)
        assertThat(responsePostTask.statusCode).isEqualTo(HttpStatus.CREATED)

        step("Update param")
        val updateRequest = ToDoDTO(
            request.id,
            request.text,
            true
        )
        val responseUpdateTask = baseService.putTasks(request.id!!, updateRequest)
        assertThat(responseUpdateTask.statusCode).isEqualTo(HttpStatus.OK)

        step("Get updated task")
        val responseGetTask = baseService.getTasks()
        assertThat(responseGetTask.statusCode).isEqualTo(HttpStatus.OK)
        val responseBody = responseGetTask.body!! as Array<ToDoDTO>
        assertThat(responseBody.find { it.id == updateRequest.id }).isEqualTo(updateRequest)
        //delete task
        createdTodoId = responseBody
    }

    @Test
    @Tag("skipCleanup")
    @DisplayName("Get list with params")
    fun getListWithParams() {
        val response = baseService.getTasks("0", "240")
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

}