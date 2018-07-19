package com.stock.kotlinhood.mapper

import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select
import java.time.LocalDateTime

//@Mapper
interface StockMapper {

    @Select("SELECT * FROM stockmarket.stocks WHERE symbol = #{symbol} order by tickedat")
    fun findBySymbolOrderByTime(@Param("symbol") symbol: String): Stock
//
//    @Insert("Insert into stocks values (#{symbol},#{openPrice},#{closePrice},#{atTime})")
//    fun insertStock(@Param("stock") stock: Stock)

}

data class Stock(val symbol: String, val openPrice: Double, val closePrice: Double, val atTime: LocalDateTime)

