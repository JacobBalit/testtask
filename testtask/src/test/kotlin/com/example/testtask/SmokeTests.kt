package com.example.testtask

import com.example.testtask.dto.ToDoDTO
import com.example.testtask.testdata.TestData.createTaskRequest
import com.example.testtask.testdata.TestData.updateTaskRequest
import io.qameta.allure.Allure.step
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.springframework.http.HttpStatus
import kotlin.random.Random.Default.nextInt

@Suppress("UNCHECKED_CAST")
@DisplayName("Tests per request /todo")
class SmokeTests : Base() {

    private var createdTodoId: Array<ToDoDTO>? = null

    @Test
    @Tag("skipCleanup")
    @DisplayName("Get list without params")
    fun getEmptyList() {
        val response = baseService.getTasks()
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body as Array<ToDoDTO>).isEmpty()
    }

    @Test
    @DisplayName("Create task and check result")
    fun postList() {
        step("Create task")
        val responsePostTask = baseService.postTasks(createTaskRequest)
        assertThat(responsePostTask.statusCode).isEqualTo(HttpStatus.CREATED)

        step("Get task")
        val responseGetTask = baseService.getTasks()
        assertThat(responseGetTask.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(responseGetTask.body).isEqualTo(arrayOf(createTaskRequest))

        //delete task
        createdTodoId = responseGetTask.body!! as Array<ToDoDTO>
    }

    @Test
    @DisplayName("Update task and check result")
    fun updateTask() {
        step("Create task")
        val responsePostTask = baseService.postTasks(createTaskRequest)
        assertThat(responsePostTask.statusCode).isEqualTo(HttpStatus.CREATED)

        step("Update task")
        val responseUpdateTask = baseService.putTasks(createTaskRequest.id, updateTaskRequest)
        assertThat(responseUpdateTask.statusCode).isEqualTo(HttpStatus.OK)

        step("Get updated task")
        val responseGetTask = baseService.getTasks()
        assertThat(responseGetTask.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(responseGetTask.body).isEqualTo(arrayOf(updateTaskRequest))

        //delete task
        createdTodoId = responseGetTask.body!! as Array<ToDoDTO>
    }

    @Test
    @DisplayName("Update task one param and check result")
    fun updateTaskOneField() {
        step("Create task")
        val responsePostTask = baseService.postTasks(createTaskRequest)
        assertThat(responsePostTask.statusCode).isEqualTo(HttpStatus.CREATED)

        step("Update param")
        val updateRequest = ToDoDTO(
            createTaskRequest.id,
            createTaskRequest.text,
            true
        )
        val responseUpdateTask = baseService.putTasks(createTaskRequest.id, updateRequest)
        assertThat(responseUpdateTask.statusCode).isEqualTo(HttpStatus.OK)

        step("Get updated task")
        val responseGetTask = baseService.getTasks()
        assertThat(responseGetTask.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(responseGetTask.body).isEqualTo(arrayOf(updateRequest))

        //delete task
        createdTodoId = responseGetTask.body!! as Array<ToDoDTO>
    }

    @Test
    @Tag("skipCleanup")
    @DisplayName("Get list with params")
    fun getListWithParams() {
        val response = baseService.getTasks("0", "240")
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

    @AfterEach
    fun cleanTasks(testInfo: TestInfo){
        if (testInfo.tags.contains("skipCleanup")) {
            println("Skipping cleanup")
            return
        }
        step("Delete task")
        val deleteTask = baseService.deleteTasks(createdTodoId!!.first().id, createdTodoId!!.first())
        assertThat(deleteTask.statusCode).isEqualTo(HttpStatus.NO_CONTENT)

        step("Check delete")
        assertThat(baseService.getTasks().body as Array<ToDoDTO>).isEmpty()
    }
}