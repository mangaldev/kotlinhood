package com.stock.kotlinhood.service

import com.stock.kotlinhood.mapper.Stock
import com.stock.kotlinhood.mapper.StockMapper
import org.springframework.stereotype.Service

@Service
class StockService(val stockRepo: StockMapper) {

    fun findBySymbol(symbol: String): Stock {
        return stockRepo.findBySymbolOrderByTime(symbol)
    }

    fun startIngestion(symbol: String) {

    }
}
