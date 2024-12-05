package com.example.testtask.dto

enum class Error(
    val errorText: String
) {
    MISSING_FIELD(errorText = "Request body deserialize error: missing field `id` at line 5 column 1")
}