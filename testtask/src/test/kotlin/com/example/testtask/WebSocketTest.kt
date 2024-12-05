package com.example.testtask


import com.example.testtask.dto.WebSocketMessageDTO
import com.example.testtask.testdata.TestData.createTaskRequest
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.engine.cio.*
import io.ktor.websocket.*
import io.qameta.allure.Allure.step
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

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
                baseService.postTasks(createTaskRequest)

                step("Expecting message")
                val incomingMessage = (incoming.receive() as Frame.Text).readText()

                step("Map and compare response")
                val expected = WebSocketMessageDTO(createTaskRequest, type = "new_todo")
                val expectedJson: JsonNode = objectMapper.valueToTree(expected)
                val actualJson: JsonNode = objectMapper.readTree(incomingMessage)
                assertThat(expectedJson).isEqualTo(actualJson)

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
