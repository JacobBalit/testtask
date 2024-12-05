package com.example.testtask.utils

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KLogging
import org.springframework.http.HttpEntity
import org.springframework.http.HttpRequest
import org.springframework.http.ResponseEntity
import org.springframework.http.client.ClientHttpResponse
import java.nio.charset.StandardCharsets.UTF_8

class LoggerUtils(
    private var objectMapper: ObjectMapper
) : KLogging() {

    fun logRequest(request: HttpEntity<*>?) {
        logger.info { "Request headers: ${request?.headers}" }
        logger.info { "Request body: ${objectMapper.writeValueAsString(request?.body ?: "")}" }
    }

    fun logRequestRestClient(request: HttpRequest?, body: ByteArray) {
        logger.info { "Request headers: ${request?.headers}" }
        logger.info { "Request: ${request?.method!!.name()} ${request.uri}" }
        logger.info { "Request body: ${String(body, UTF_8)}" }
    }

    fun logResponse(response: ResponseEntity<*>) =
        logger.info { "Response body: ${objectMapper.writeValueAsString(response.body ?: "")}" }

    fun logResponseRestClient(response: ClientHttpResponse?) {
        logger.info { "Response status: ${response?.statusCode}" }
        logger.info { "Response headers: ${response?.headers}" }
    }
}
