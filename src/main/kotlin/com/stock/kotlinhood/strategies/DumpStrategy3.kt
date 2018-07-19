package com.stock.kotlinhood.strategies

import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import java.io.File
import java.nio.charset.Charset
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


/**
 *                  Profit             first_stop|running_stop|loss_threshold
 * GOOGL -> 2 weeks Profit  : 49.287   .015/0.8/0.015
 * GOOGL -> 2 weeks Profit  : 55.287   .015/0.8/0.025

 */
class DumpStrategy3 {
    companion object {

        var profit = 0.0
        var lastBoughtAt = 0.0
        var hasBought = false
        var stopLoss = 0.0
        val STOP_LOSS_UP_MARGIN = 0.015
        val STOP_LOSS_RUNNING_UP_MARGIN = 0.75
        val STOP_LOSS_DOWN_MARGIN = 0.50
        var positiveTrend = 0
        var lastQuote = 0.0
        var buyCounter = 0
        var totalProfit = 0.0
        var currentDateTime: LocalDateTime = LocalDateTime.MAX
        val stocks = listOf<String>("CSCO")
        val tillDate = "05-01-18"
        val stockSpread = "5min"
        @JvmStatic
        fun main(args: Array<String>) {
            stocks.forEach { stock ->
                hasBought = false
                profit = 0.0
                lastBoughtAt = 0.0
                stopLoss = 0.0
                positiveTrend = 0
                lastQuote = 0.0
                buyCounter = 0
                val fromJson = Gson().fromJson<LinkedTreeMap<String, Any>>(
                    File("/Users/mdev/Google Drive/TextWrangler/robinhood/$stock-2weeks-$stockSpread-$tillDate.json").readText(Charset.defaultCharset()),
                    Map::class.java
                )
                val data = fromJson["Time Series ($stockSpread)"] as LinkedTreeMap<String, Any>
                data.keys.reversed().forEach {
                    currentDateTime = LocalDateTime.parse(it, DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss"))
                    val closingPrice = ((data[it] as LinkedTreeMap<String, Any>)["4. close"]).toString().toDouble()
                    println("closingPrice = ${closingPrice}")
                    if (!hasBought) {
                        buy(closingPrice)
//            targetStopLoss(closingPrice)
                    } else if (!stopLossIsNotSet() && closingPrice < stopLoss) {
                        println("$closingPrice is less than stopLoss : $stopLoss, so will be sold.")
                        sell(closingPrice)
                    } else if (closingPrice > lastBoughtAt){//|| (!stopLossIsNotSet() && closingPrice > stopLoss)) {
                        assignStopLoss(
                            lastBoughtAt,
                            closingPrice
                        )
                    } else {
                        if (crossedBelowMargin(
                                lastBoughtAt,
                                closingPrice
                            )
                        ) {
                            sell(closingPrice)
                        }
                    }
                    lastQuote = closingPrice
                }
                totalProfit += profit
                println(
                    "Profit for $stock is $profit",
                    true
                )
                println(
                    "Number of times stock bought $buyCounter",
                    true
                )
            }
            println(
                "Total Profit for all stocks is $totalProfit",
                true
            )

        }

        fun crossedBelowMargin(lastBoughtAt: Double, closingPrice: Double): Boolean {
            val downMargin = lastBoughtAt - closingPrice
            assert(downMargin > 0) { print("Checking down stop loss closingPrice shoudld be lesser than lastBoughtAt") }
            if (downMargin > (lastBoughtAt * (STOP_LOSS_DOWN_MARGIN / 100)))
                return true
            return false
        }

        private fun sell(closingPrice: Double) {
            if (stopLossIsNotSet()) {
                val losss = lastBoughtAt - closingPrice
                profit -= losss
                println("Selling at $closingPrice  with  LOSS of $losss")
            } else {
                val profitt = stopLoss - lastBoughtAt
                profit += profitt
                println("Selling at $stopLoss with PROFIT of $profitt")
            }
            println("net profit = $profit")
            clearStopLossIfAny() //This would be done automatically
            clearLastBoughtAt()
        }

        fun clearLastBoughtAt() {
            lastBoughtAt = 0.0
            hasBought = false
        }

        fun clearStopLossIfAny() {
            stopLoss = 0.0

        }

        private fun stopLossIsNotSet() = stopLoss == 0.0

        fun assignStopLoss(lastBoughtAt: Double, closingPrice: Double) {
            val currentMargin = closingPrice - lastBoughtAt
            assert(currentMargin > 0) { print("Assigning stop loss closingPrice shoudld be greater than lastBoughtAt") }
//            if (stopLossIsNotSet()) {
//                if (currentMargin > (lastBoughtAt * (STOP_LOSS_UP_MARGIN / 100))) {
//                    clearStopLossIfAny()
//                    stopLoss = lastBoughtAt + (lastBoughtAt * (STOP_LOSS_UP_MARGIN / 100))
////
//                    println("Setting stopLoss at ${stopLoss}")
//                }
//            } else {
//                clearStopLossIfAny()
//                stopLoss = lastBoughtAt + (currentMargin * STOP_LOSS_RUNNING_UP_MARGIN)
//                println("Setting stopLoss at ${stopLoss}")
//            }
            val newstopLoss = lastBoughtAt + (currentMargin * STOP_LOSS_RUNNING_UP_MARGIN)
            if (newstopLoss > stopLoss) {
                stopLoss = newstopLoss
                println("Setting stopLoss at $stopLoss")
            }
        }

        fun buy(currentPrice: Double) {

            if (currentPrice > lastQuote) positiveTrend++
            else {
                positiveTrend = 0
            }
            if (positiveTrend > 2) {
//                if (currentDateTime.hour == 15 && currentDateTime.minute > 20)
//                    return
                println("Buying at $currentPrice")
                lastBoughtAt = currentPrice
                hasBought = true
                positiveTrend = 0
                buyCounter++
            }
        }

        private fun println(message: Any, show: Boolean = true) {
            if (show)
                System.out.println(message)
        }
    }


}

