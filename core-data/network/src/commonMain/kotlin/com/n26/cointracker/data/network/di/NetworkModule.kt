package com.n26.cointracker.data.network.di

import com.n26.cointracker.data.network.datasource.PriceRemoteDataSource
import com.n26.cointracker.data.network.datasource.PriceRemoteDataSourceImpl
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import co.touchlab.kermit.Logger as KermitLogger

val networkModule = module {

    single {
        val json = Json { ignoreUnknownKeys = true }
        HttpClient {
            install(ContentNegotiation) {
                json(json, contentType = ContentType.Application.Json)
            }

            install(Logging) {
                level = LogLevel.HEADERS
                logger = object : Logger {
                    override fun log(message: String) {
                        KermitLogger.d("Http Client: $message")
                    }
                }
            }
        }
    }

    single<PriceRemoteDataSource> { PriceRemoteDataSourceImpl(get()) }

}
