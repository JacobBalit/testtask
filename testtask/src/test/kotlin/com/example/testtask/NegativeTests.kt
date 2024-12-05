package com.example.testtask

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.http.HttpStatus

@DisplayName("Negative tests per request /todo")
class NegativeTests: Base() {

    companion object {
        @JvmStatic
        fun dataProvider(): List<Array<Any?>> {
            return listOf(
                arrayOf("123", "30242453535345678909876"),
                arrayOf("-1243414", "-213213"),
                arrayOf("null", "null"),
                arrayOf("/*/", "#%^")

            )
        }
    }
    @ParameterizedTest
    @MethodSource("dataProvider")
    @DisplayName("Get list with params")
    fun getListWithNegativeParams(offset: String?, limit: String?) {
        val response = baseService.getTasks(offset, limit)
        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(response.body).isEqualTo("Invalid query string")
    }
}