package com.stock.kotlinhood.models


data class DataUnit(
    private val open: Double = 0.toDouble(),
    private val high: Double = 0.toDouble(),
    private val low: Double = 0.toDouble(),
    private val close: Double = 0.toDouble(),
    private val volume: Double = 0.toDouble()
)
