package com.stock.kotlinhood.controller

import com.stock.kotlinhood.mapper.Stock
import com.stock.kotlinhood.mapper.StockMapper
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/stock")
class StockController(val stockMapper: StockMapper) {

    @GetMapping("/{symbol}")
    fun info(@PathVariable symbol: String): Set<Stock> {
        println("Looking for the eestock : $symbol")
        return stockMapper.findBySymbol(symbol)
    }

}