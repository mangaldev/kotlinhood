package com.stock.kotlinhood.service

import com.github.kittinunf.fuel.Fuel
import com.github.rholder.retry.RetryerBuilder
import com.github.rholder.retry.StopStrategies
import com.github.rholder.retry.WaitStrategies
import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import com.stock.kotlinhood.mapper.Quote
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

@Service
class AlphavantageAPI(
    private val alphavantageParameters: AlphavantageParameters
) : StockAPIService {

    override fun getQuotesFor(symbol: String): Set<Quote> {

        val jsonResponse = RetryerBuilder.newBuilder<String>()
            .withWaitStrategy(WaitStrategies.fibonacciWait(10, 10, TimeUnit.SECONDS))
            .retryIfResult { it.isNullOrEmpty() }
            .retryIfExceptionOfType(IOException::class.java)
            .retryIfRuntimeException()
            .withStopStrategy(StopStrategies.stopAfterAttempt(10))
            .build().call {
                val (request, response, result) = Fuel.get(
                    alphavantageParameters.apiurl, listOf(
                        "symbol" to symbol,
                        "function" to alphavantageParameters.function,
                        "interval" to alphavantageParameters.interval,
                        "outputsize" to alphavantageParameters.outputsize,
                        "apikey" to alphavantageParameters.apikey
                    )
                ).responseString()
                result.get()
            }
        val fromJson = Gson().fromJson<LinkedTreeMap<String, Any>>(
            jsonResponse.toString(),
            Map::class.java
        )
        val data = fromJson["Time Series (${alphavantageParameters.interval})"] as LinkedTreeMap<String, Any>
        return data.keys.map {
            val currentDateTime = LocalDateTime.parse(it, DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss"))
            val openPrice = ((data[it] as LinkedTreeMap<String, Any>)["1. open"]).toString().toDouble()
            val highPrice = ((data[it] as LinkedTreeMap<String, Any>)["2. high"]).toString().toDouble()
            val lowPrice = ((data[it] as LinkedTreeMap<String, Any>)["3. low"]).toString().toDouble()
            val closePrice = ((data[it] as LinkedTreeMap<String, Any>)["4. close"]).toString().toDouble()
            val volume = ((data[it] as LinkedTreeMap<String, Any>)["5. volume"]).toString().toLong()
            Quote(symbol, openPrice, closePrice, lowPrice, highPrice, currentDateTime, currentDateTime, volume)
        }.toSet()

    }
}

@Component
@ConfigurationProperties(prefix = "alphavantange")
class AlphavantageParameters() {
    lateinit var apikey: String
    lateinit var apiurl: String
    lateinit var interval: String
    lateinit var outputsize: String
    lateinit var function: String
}
