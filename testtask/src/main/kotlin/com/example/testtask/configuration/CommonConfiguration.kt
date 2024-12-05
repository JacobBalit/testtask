package com.example.testtask.configuration

import org.apache.hc.client5.http.cookie.CookieStore
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder
import org.apache.hc.client5.http.impl.io.BasicHttpClientConnectionManager
import org.apache.hc.client5.http.socket.ConnectionSocketFactory
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory
import org.apache.hc.core5.http.config.Registry
import org.apache.hc.core5.http.config.RegistryBuilder
import org.apache.hc.core5.ssl.SSLContexts
import org.apache.hc.core5.ssl.TrustStrategy
import javax.net.ssl.SSLContext

object CommonConfiguration {

    fun connectionManagerSSLAcceptAll(): BasicHttpClientConnectionManager {
        val acceptingTrustStrategy = TrustStrategy { _, _ -> true }
        val sslContext: SSLContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build()
        val sslSf = SSLConnectionSocketFactory(
            sslContext,
            NoopHostnameVerifier.INSTANCE
        )
        val socketFactoryRegistry: Registry<ConnectionSocketFactory> = RegistryBuilder.create<ConnectionSocketFactory>()
            .register("https", sslSf)
            .register("http", PlainConnectionSocketFactory())
            .build()

        return BasicHttpClientConnectionManager(socketFactoryRegistry)
    }

    fun httpClient(
        cookieStore: CookieStore,
        connectionManager: BasicHttpClientConnectionManager,
    ): CloseableHttpClient =
        HttpClientBuilder
            .create()
            .disableRedirectHandling()
            .setDefaultCookieStore(cookieStore)
            .setConnectionManager(connectionManager)
            .build()
}