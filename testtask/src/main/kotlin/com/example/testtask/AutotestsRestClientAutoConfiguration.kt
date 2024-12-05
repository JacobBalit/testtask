package com.example.testtask

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.hc.client5.http.cookie.CookieStore
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestClient
import org.springframework.web.util.DefaultUriBuilderFactory
import com.example.testtask.configuration.CommonConfiguration.connectionManagerSSLAcceptAll
import com.example.testtask.configuration.CommonConfiguration.httpClient
import com.example.testtask.utils.LoggerUtils
import java.time.Duration

@AutoConfiguration
class AutotestsRestClientAutoConfiguration {

    @Bean
    fun configureRestClient(
        objectMapper: ObjectMapper,
        cookieStore: CookieStore,
    ): RestClient {
        val connectionManagerSSLAcceptAll = connectionManagerSSLAcceptAll()
        val httpClient = httpClient(cookieStore, connectionManagerSSLAcceptAll)
        val factory = HttpComponentsClientHttpRequestFactory(httpClient).apply {
            setConnectTimeout(Duration.ofSeconds(10))
            setConnectionRequestTimeout(Duration.ofSeconds(10))
        }
        val defaultUriBuilderFactory = DefaultUriBuilderFactory()
            .apply { encodingMode = DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY }

        return RestClient.builder()
            .requestFactory(factory)
            .requestInterceptor { request, body, execution ->
                LoggerUtils(objectMapper).logRequestRestClient(request, body)
                val response = execution.execute(request, body)
                LoggerUtils(objectMapper).logResponseRestClient(response)
                response
            }
            .uriBuilderFactory(defaultUriBuilderFactory)
            .messageConverters {
                it.add(0, MappingJackson2HttpMessageConverter().apply { this.objectMapper = objectMapper })
            }
            .build()
    }
}