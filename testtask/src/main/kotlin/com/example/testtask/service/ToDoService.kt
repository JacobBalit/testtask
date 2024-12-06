package com.example.testtask.service

import com.example.testtask.dto.ToDoDTO
import com.example.testtask.utils.exchangeIf2xx
import com.example.testtask.utils.paramsToRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient

@Service
class ToDoService(
    @Autowired private val restClient: RestClient
) {

    @Value("\${base-url.todoservice}")
    lateinit var toDoServiceUrl: String

    private fun pathTodoList(): String = "$toDoServiceUrl/todos"
    private fun pathPutTodoList(id: Int?): String = "$toDoServiceUrl/todos/$id"

    fun getTasks(
        offset: String? = null,
        limit: String? = null
    ): ResponseEntity<*> {
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
                    String::class
                )
            } as ResponseEntity<*>
    }

    fun postTasks(
        request: ToDoDTO
    ): ResponseEntity<*> {
        return restClient.post()
            .uri(pathTodoList())
            .body(request)
            .exchange { _, response ->
                exchangeIf2xx(
                    response,
                    String::class,
                    String::class
                )
            } as ResponseEntity<*>
    }

    fun putTasks(
        id: Int,
        request: ToDoDTO,
        auth: String = "YWRtaW46YWRtaW4="
    ): ResponseEntity<*> {
        return restClient.put()
            .uri(pathPutTodoList(id))
            .headers {
                it
                    .setBasicAuth(auth)
            }
            .body(request)
            .exchange { _, response ->
                exchangeIf2xx(
                    response,
                    Array<ToDoDTO>::class,
                    String::class
                )
            } as ResponseEntity<*>
    }

    fun deleteTasks(
        id: Int?,
        auth: String = "YWRtaW46YWRtaW4="
    ): ResponseEntity<*> {
        return restClient.delete()
            .uri(pathPutTodoList(id))
            .headers {
                it
                    .setBasicAuth(auth)
            }
            .exchange { _, response ->
                exchangeIf2xx(
                    response,
                    String::class,
                    String::class
                )
            } as ResponseEntity<*>
    }
}