package com.example.testtask


import com.example.testtask.dto.ToDoDTO
import com.example.testtask.dto.WebSocketMessageDTO
import com.fasterxml.jackson.databind.JsonNode
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.websocket.*
import io.ktor.websocket.*
import io.qameta.allure.Allure.step
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.random.Random.Default.nextInt

@Suppress("UNCHECKED_CAST")
@DisplayName("WebSocketTest")
class WebSocketTest: Base() {

    @Test
    fun testWebSocketConnection() = runBlocking {
        val client = HttpClient(CIO) {
            install(WebSockets)
        }

        try {
            step("Connecting to WS")
            client.webSocket(host = "localhost", port = 8080, path = "/ws") {

                step("Send message")
                val request = ToDoDTO(
                    id = nextInt(1, Int.MAX_VALUE),
                    text = "TestTask" + nextInt(1, Int.MAX_VALUE),
                    completed = false
                )
                baseService.postTasks(request)
                val getTask = baseService.getTasks().body!! as Array<ToDoDTO>

                step("Expecting message")
                val incomingMessage = (incoming.receive() as Frame.Text).readText()

                step("Map and compare response")
                val expected = WebSocketMessageDTO(getTask.find { it.id == request.id }, type = "new_todo")
                val expectedJson: JsonNode = objectMapper.valueToTree(expected)
                val actualJson: JsonNode = objectMapper.readTree(incomingMessage)
                assertThat(expectedJson).isEqualTo(actualJson)

                //delete task
                createdTodoId = getTask

                step("Close connection")
                close(CloseReason(CloseReason.Codes.NORMAL, "Test complete"))
                println("Connection closed.")
            }
        } catch (e: Exception) {
            println("Error: ${e.message}")
        } finally {
            client.close()
        }
    }
}
