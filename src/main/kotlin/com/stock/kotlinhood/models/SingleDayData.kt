package com.stock.kotlinhood.models


class SingleDayData {
    private val company: String? = null
    // in minutes
    private val interval: Int = 0

    internal var timeSeriesData: List<DataUnit>? = null
}
