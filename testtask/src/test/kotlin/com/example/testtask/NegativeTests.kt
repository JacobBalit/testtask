package com.example.testtask

import com.example.testtask.dto.ToDoDTO
import com.example.testtask.testdata.TestData.updateTaskRequest
import io.qameta.allure.Allure.step
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.NullSource
import org.springframework.http.HttpStatus
import kotlin.random.Random.Default.nextInt
import kotlin.test.Test

@Suppress("UNCHECKED_CAST")
@DisplayName("Negative tests per request /todo")
class NegativeTests : Base() {

    @ParameterizedTest
    @MethodSource("dataProviderStrings")
    @Tag("skipCleanup")
    @DisplayName("[negative]  Get list with params")
    fun getListWithNegativeParams(offset: String?, limit: String?) {
        val response = baseService.getTasks(offset, limit)
        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(response.body).isEqualTo("Invalid query string")
    }

    @ParameterizedTest(name = "id = {0}")
    @MethodSource("dataProviderInt")
    @Tag("skipCleanup")
    @DisplayName("[negative] Create task invalid params")
    fun postListInvalidId(id: Int?) {
        step("Create task")
        val responsePostTask = baseService.postTasks(ToDoDTO(id, "InvalidTask", true))
        assertThat(responsePostTask.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @ParameterizedTest(name = "text = {0}")
    @NullSource
    @Tag("skipCleanup")
    @DisplayName("[negative] Create task invalid params")
    fun postListInvalidText(text: String?) {
        step("Create task")
        val responsePostTask = baseService.postTasks(ToDoDTO(id = nextInt(1, Int.MAX_VALUE), text, true))
        assertThat(responsePostTask.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    @DisplayName("[negative] Create task invalid params")
    fun postListDuplicateTask() {
        step("Create task")
        val request = ToDoDTO(
            id = nextInt(1, Int.MAX_VALUE),
            text = "TestTask" + nextInt(1, Int.MAX_VALUE),
            completed = false
        )
        val responsePostTask = baseService.postTasks(request)
        assertThat(responsePostTask.statusCode).isEqualTo(HttpStatus.CREATED)

        step("Create duplicate task")
        val responsePostTask1 = baseService.postTasks(request)
        assertThat(responsePostTask1.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)

        //delete task
        createdTodoId = arrayOf(request)
    }

    @Test
    @Tag("skipCleanup")
    @DisplayName("[negative]  Update task with invalid id")
    fun updateTaskNotExist() {
        step("Update task")
        val responseUpdateTask = baseService.putTasks(nextInt(1, Int.MAX_VALUE), updateTaskRequest)
        assertThat(responseUpdateTask.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }

    @Test
    @DisplayName("[negative] Update task with invalid body")
    fun updateTaskInvalidBody() {
        step("Create task")
        val request = ToDoDTO(
            id = nextInt(1, Int.MAX_VALUE),
            text = "TestTask" + nextInt(1, Int.MAX_VALUE),
            completed = false
        )

        val responsePostTask = baseService.postTasks(request)
        assertThat(responsePostTask.statusCode).isEqualTo(HttpStatus.CREATED)

        step("Update task")
        val responseUpdateTask = baseService.putTasks(request.id!!, ToDoDTO(null, "text", true))
        assertThat(responseUpdateTask.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        //delete task
        val getTask = baseService.getTasks()
        createdTodoId = getTask.body as Array<ToDoDTO>
    }

    @Test
    @Tag("skipCleanup")
    @DisplayName("Delete task with invalid Id")
    fun deleteTaskInvalidId() {
        step("Delete task")
        val deleteTask = baseService.deleteTasks(nextInt(1, Int.MAX_VALUE))
        assertThat(deleteTask.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }

    @Test
    @Tag("skipCleanup")
    @DisplayName("[negative] Delete task Unauthorized")
    fun deleteTaskUnauthorized() {
        step("Delete task")
        val deleteTask = baseService.deleteTasks(nextInt(1, Int.MAX_VALUE), "root")
        assertThat(deleteTask.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
    }

    @Test
    @Tag("skipCleanup")
    @DisplayName("[negative] Put task Unauthorized")
    fun putTaskUnauthorized() {
        step("Delete task")
        val value = nextInt(1, Int.MAX_VALUE)
        val deleteTask = baseService.putTasks(value, ToDoDTO(value, "text", true), "root")
        assertThat(deleteTask.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED) // BUG - Auth check should first, and then validation
    }

    companion object {
        @JvmStatic
        fun dataProviderStrings(): List<Array<Any?>> {
            return listOf(
                arrayOf("30242453535345678909876302424535353456789098763024245353534567890987630242453535345678909876", "23"),
                arrayOf("0", "-213213")
            )
        }

        @JvmStatic
        fun dataProviderInt(): List<Array<Any?>> {
            return listOf(
                arrayOf(null, 3024245353534567890),
                arrayOf("-1243414", "-213213")
            )
        }
    }
}