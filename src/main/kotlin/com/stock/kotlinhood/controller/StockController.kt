package com.stock.kotlinhood.controller

import com.stock.kotlinhood.service.StockService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/stock")
class StockController(val stockService: StockService) {

    @GetMapping("/info/{symbol}")
    fun info(@PathVariable symbol: String) {
        println("Looking for the eestock : $symbol")
        stockService.findBySymbol(symbol)
    }

    @PutMapping(("/ingestion/start/{symbol}"))
    fun startIngestion(@PathVariable symbol: String): String {
        stockService.startIngestion(symbol)
        return "Starting ingestion for $symbol"
    }
}