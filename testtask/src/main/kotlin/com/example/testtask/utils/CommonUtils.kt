package com.example.testtask.utils

import java.net.URLEncoder.encode
import kotlin.text.Charsets.UTF_8

fun paramsToRequest(urlVariables: Map<String, *>): String {
    val params = urlVariables.entries.joinToString("&") { (key, value) ->
        when (value) {
            is Iterable<*> -> value.joinToString("&") { item ->
                "$key=${encode(item.toString(), UTF_8.name())}"
            }

            else -> "$key=${encode(value.toString(), UTF_8.name())}"
        }
    }
    return if (params.isNotEmpty()) "?$params" else ""
}