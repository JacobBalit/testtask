package com.example.testtask.service

import com.example.testtask.dto.Error
import com.example.testtask.dto.ToDoDTO
import com.example.testtask.utils.exchangeIf2xx
import com.example.testtask.utils.paramsToRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient

@Suppress("UNCHECKED_CAST")
@Service
class ToDoService(
    @Autowired private val restClient: RestClient
) {

    @Value("\${base-url.todoservice}")
    lateinit var toDoServiceUrl: String

    private fun pathTodoList(): String = "$toDoServiceUrl/todos"
    private fun pathPutTodoList(id: Int): String = "$toDoServiceUrl/todos/$id"

    fun getTasks(
        offset: String? = null,
        limit: String? = null
    ): ResponseEntity<Array<ToDoDTO>> {
        val queryParam = mutableMapOf<String, String>().apply {
            offset?.let { put("offset", it) }
            limit?.let { put("limit", it) }
        }
        return restClient.get()
            .uri(pathTodoList() + paramsToRequest(queryParam))
            .exchange { _, response ->
                exchangeIf2xx(
                    response,
                    Array<ToDoDTO>::class,
                    Error::class
                )
            }  as ResponseEntity<Array<ToDoDTO>>
    }

    fun postTasks(
        request: ToDoDTO
    ): ResponseEntity<Array<ToDoDTO>> {
        return restClient.post()
            .uri(pathTodoList())
            .body(request)
            .exchange { _, response ->
                exchangeIf2xx(
                    response,
                    Array<ToDoDTO>::class,
                    Error::class
                )
            } as ResponseEntity<Array<ToDoDTO>>
    }

    fun putTasks(
        id: Int,
        request: ToDoDTO
    ): ResponseEntity<*> {
        return restClient.put()
            .uri(pathPutTodoList(id))
            .body(request)
            .exchange { _, response ->
                exchangeIf2xx(
                    response,
                    Array<ToDoDTO>::class,
                    Array<ToDoDTO>::class
                )
            } as ResponseEntity<Array<ToDoDTO>>
    }

    fun deleteTasks(
        id: Int,
        request: ToDoDTO
    ): ResponseEntity<*> {
        return restClient.delete()
            .uri(pathPutTodoList(id))
            .headers{
                it
                    .setBasicAuth("YWRtaW46YWRtaW4=")
            }
            .exchange { _, response ->
                exchangeIf2xx(
                    response,
                    String::class,
                    Error::class
                )
            } as ResponseEntity<Array<ToDoDTO>>
    }
}