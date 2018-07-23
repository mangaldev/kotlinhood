package com.stock.kotlinhood.mapper

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.time.LocalDateTime


@Repository
class QuoteMapper(@Qualifier("jdbcTemplate") val jdbcTemplate: JdbcTemplate) {

    fun insert(quote: Quote): Int {
        return jdbcTemplate.update(
            "INSERT INTO stock_schema.quote\n" +
                    "(symbol, openprice, closeprice, lowprice, highprice, beginsat, closesat, volume)\n" +
                    "VALUES(?, ?, ?, ?, ?, ?, ?, ? ) ON CONFLICT DO NOTHING",
            quote.symbol, quote.openPrice, quote.closePrice, quote.lowPrice, quote.highPrice, quote.beginsAt, quote.closesAt, quote.volume
        )
    }

    fun quotesBy(symbol: String): Set<Quote> {
        return jdbcTemplate.query("SELECT  * from stock_schema.quote")
        { rs: ResultSet, _: Int ->
            Quote(
                rs.getString(1),
                rs.getDouble(2),
                rs.getDouble(3),
                rs.getDouble(4),
                rs.getDouble(5),
                rs.getTimestamp(6).toLocalDateTime(),
                rs.getTimestamp(7).toLocalDateTime(),
                rs.getLong(8),
                rs.getTimestamp(7).toLocalDateTime()
            )
        }.toSet()
    }
}


data class Quote(
    val symbol: String,
    val openPrice: Double,
    val closePrice: Double,
    val lowPrice: Double = 0.0,
    val highPrice: Double = 0.0,
    val beginsAt: LocalDateTime = LocalDateTime.now(),
    val closesAt: LocalDateTime = LocalDateTime.now(),
    val volume: Long = 0,
    val createdAt: LocalDateTime = LocalDateTime.now()
)