package com.example.testtask.testdata

import com.example.testtask.dto.ToDoDTO
import kotlin.random.Random.Default.nextInt

object TestData {

    const val ERROR_RESPONSE = "Invalid query string"

    val createTaskRequest = ToDoDTO(
        id = nextInt(1, Int.MAX_VALUE),
        text = "TestTask" + nextInt(1, Int.MAX_VALUE),
        completed = false
    )

    val updateTaskRequest = ToDoDTO(
        id = nextInt(1, Int.MAX_VALUE),
        text = "TestTask" + nextInt(1, Int.MAX_VALUE),
        completed = true
    )
}