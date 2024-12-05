package com.example.testtask.utils

import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestClient.RequestHeadersSpec.ConvertibleClientHttpResponse
import kotlin.reflect.KClass

fun convertToResponseEntity(
    response: ConvertibleClientHttpResponse,
    bodyType: Class<*>
): ResponseEntity<*> =
    ResponseEntity
        .status(response.statusCode)
        .headers(response.headers)
        .body(response.bodyTo(bodyType))

fun createUriWithParams(
    path: String,
    queryParams: Map<String, *>? = null
): String =
    "$path${queryParams?.let { paramsToRequest(it) }}"

fun exchangeIf2xx(
    response: ConvertibleClientHttpResponse,
    successClass: KClass<*>,
    errorClass: KClass<*>
): ResponseEntity<*> {
    return if (response.statusCode.is2xxSuccessful) {
        convertToResponseEntity(response, successClass.java)
    } else {
        convertToResponseEntity(response, errorClass.java)
    }
}
