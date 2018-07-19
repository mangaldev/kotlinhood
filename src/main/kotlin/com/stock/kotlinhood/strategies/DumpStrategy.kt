package com.stock.kotlinhood.strategies

import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import java.io.File
import java.nio.charset.Charset


/**
 *                              Proft   first_stop|running_stop|loss_threshold
 * GOOGL -> 2 weeks Profit  : 49.287   .015/0.8/0.015
 * GOOGL -> 2 weeks Profit  : 55.287   .015/0.8/0.025

 */
var profit = 0.0
var lastBoughtAt = 0.0
var hasBought = false
var stopLoss = 0.0
val STOP_LOSS_UP_MARGIN = 0.015
val STOP_LOSS_RUNNING_UP_MARGIN = 0.8
val STOP_LOSS_DOWN_MARGIN = 0.025
var positiveTrend = 0
var lastQuote = 0.0

fun main(args: Array<String>) {

    val fromJson = Gson().fromJson<LinkedTreeMap<String, Any>>(
        File("/Users/mdev/Google Drive/TextWrangler/robinhood/9Apr/AMZN-2weeks-5Min.json").readText(Charset.defaultCharset()),
//        File("/Users/mdev/Google Drive/TextWrangler/robinhood/GOOG-2weeks-1Min.json").readText(Charset.defaultCharset()),
//        File("/Users/mdev/Google Drive/TextWrangler/robinhood/MSFT-2weeks-1Min.json").readText(Charset.defaultCharset()),
//        File("/Users/mdev/Google Drive/TextWrangler/robinhood/AMZN-2weeks-1Min.json").readText(Charset.defaultCharset()),
        Map::class.java
    )
    val data = fromJson["Time Series (5min)"] as LinkedTreeMap<String, Any>
    data.keys.reversed().forEach {

        val closingPrice = ((data[it] as LinkedTreeMap<String, Any>)["4. close"]).toString().toDouble()
        println("closingPrice = ${closingPrice}")
        if (!hasBought) {
            buy(closingPrice)
//            targetStopLoss(closingPrice)
        } else if (closingPrice > lastBoughtAt) {
            if (stopLossIsNotSet()) {
                assignStopLoss(lastBoughtAt, closingPrice)
            } else {
                if (closingPrice < stopLoss) {
                    sell(closingPrice)
                } else {
                    assignStopLoss(lastBoughtAt, closingPrice)
                }
            }
        } else {
            if (crossedBelowMargin(lastBoughtAt, closingPrice)) {
                sell(closingPrice)
            }
        }
        lastQuote = closingPrice
    }
    println(profit)

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
    println("profit = $profit")
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
    if (stopLossIsNotSet()) {
        if (currentMargin > (lastBoughtAt * (STOP_LOSS_UP_MARGIN / 100))) {
            clearStopLossIfAny()
            stopLoss = lastBoughtAt + (lastBoughtAt * (STOP_LOSS_UP_MARGIN / 100))
//
            println("Setting stopLoss at $stopLoss")
        }
    } else {
        if (currentMargin > (lastBoughtAt * (STOP_LOSS_RUNNING_UP_MARGIN / 100))) {
            clearStopLossIfAny()
            stopLoss = lastBoughtAt + (lastBoughtAt * (STOP_LOSS_RUNNING_UP_MARGIN / 100))
            println("Setting stopLoss at $stopLoss")
        }
    }
//    stopLoss = lastBoughtAt + (currentMargin * (STOP_LOSS_RUNNING_UP_MARGIN / 100))
//    println("Setting stopLoss at $stopLoss")
}

fun buy(currentPrice: Double) {
    //if(time < 3:30pm and time > 10am)
    if (currentPrice > lastQuote) positiveTrend++
    else {
        positiveTrend = 0
    }
    if (positiveTrend > 2) {
        println("Buying at $currentPrice")
        lastBoughtAt = currentPrice
        hasBought = true
    }
}
