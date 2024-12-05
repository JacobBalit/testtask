package com.example.testtask

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.springframework.http.HttpStatus

@DisplayName("Negative tests per request /todo")
class NegativeTests: Base() {

    companion object {
        @JvmStatic
        fun dataProvider(): List<Array<Any>> {
            return listOf(
                arrayOf(1, "one"),
                arrayOf(2, "two"),
                arrayOf(3, "three")
            )
        }
    }
    @ParameterizedTest
    @DisplayName("Get list with params")
    fun getListWithNegativeParams() {
        val response = baseService.getTasks("0", "240")
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }
}