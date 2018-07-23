package com.stock.kotlinhood.controller

import com.stock.kotlinhood.mapper.Quote
import com.stock.kotlinhood.mapper.QuoteMapper
import com.stock.kotlinhood.mapper.StockMapper
import com.stock.kotlinhood.service.StockAPIService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/quote")
class QuoteController(
    val quoteMapper: QuoteMapper,
    val stockMapper: StockMapper, @Qualifier("alpinoStockAPI") val stockAPIService: StockAPIService
) {

    @PostMapping
    fun info(@RequestBody quote: Quote): Int {
        println("Saving Quote : $quote")
        return quoteMapper.insert(quote)
    }

    @GetMapping("/ingestion/start")
    fun startIngestion() {
        stockMapper.findAll().forEach {
            stockAPIService.getQuotesFor(it.symbol).forEach {
                println("quote to insert = ${it}")
                quoteMapper.insert(it)
            }
        }
    }
}