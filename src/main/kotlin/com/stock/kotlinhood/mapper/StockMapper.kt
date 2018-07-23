package com.stock.kotlinhood.mapper

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.query
import org.springframework.stereotype.Repository
import java.sql.ResultSet


@Repository
class StockMapper(@Qualifier("jdbcTemplate") val jdbcTemplate: JdbcTemplate) {

    fun findBySymbol(symbol: String): Set<Stock> {
        return jdbcTemplate.query("SELECT  * from stock_schema.stock where symbol = ?", symbol)
        { rs: ResultSet, _: Int ->
            Stock(
                rs.getString(1),
                rs.getString(2)

            )
        }.toSet()
    }

    fun findAll(): Set<Stock> {
        return jdbcTemplate.query("SELECT  * from stock_schema.stock")
        { rs: ResultSet, _: Int ->
            Stock(
                rs.getString(1),
                rs.getString(2)

            )
        }.toSet()
    }
}


data class Stock(val symbol: String, val name: String)