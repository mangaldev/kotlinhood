package com.stock.kotlinhood.service

import com.stock.kotlinhood.mapper.Quote

interface StockAPIService {
    fun getQuotesFor(symbol: String):Set<Quote>

}