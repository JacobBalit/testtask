package com.example.testtask.testdata

import com.example.testtask.dto.ToDoDTO
import kotlin.random.Random

object TestData {

    const val ERROR_RESPONSE = "Invalid query string"

    val createTaskRequest = ToDoDTO(
        id = Random.nextInt(1, Int.MAX_VALUE),
        text = "TestTask" + Random.nextInt(1, Int.MAX_VALUE),
        completed = false
    )

    val updateTaskRequest = ToDoDTO(
        id = Random.nextInt(1, Int.MAX_VALUE),
        text = "TestTask" + Random.nextInt(1, Int.MAX_VALUE),
        completed = true
    )
}