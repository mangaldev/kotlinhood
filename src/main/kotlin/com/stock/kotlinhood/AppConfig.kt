package com.stock.kotlinhood

import com.stock.kotlinhood.controller.QuoteController
import com.stock.kotlinhood.mapper.QuoteMapper
import com.stock.kotlinhood.mapper.StockMapper
import com.stock.kotlinhood.service.AlphavantageAPI
import com.stock.kotlinhood.service.AlphavantageParameters
import com.stock.kotlinhood.service.StockAPIService
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.web.client.RestTemplate
import javax.sql.DataSource


@Configuration
@ComponentScan
@EnableTransactionManagement
class AppConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.kotlinhood")
    fun dataSource(): DataSource = DataSourceBuilder.create().build()


    @Bean
    fun transactionManager(dataSource: DataSource) = DataSourceTransactionManager(dataSource)

    @Bean
    fun jdbcTemplate(dataSource: DataSource): JdbcTemplate {
        return JdbcTemplate(dataSource)
    }

    @Bean
    fun alpinoStockAPI(alphavantageParameters: AlphavantageParameters): StockAPIService {
        return AlphavantageAPI(alphavantageParameters)
    }


    @Scheduled(cron = "\${cron.ingestion.expression}")
    fun ingest(quoteService: QuoteMapper, stockMapper: StockMapper, stockAPIService: StockAPIService) {
        QuoteController(quoteService, stockMapper, stockAPIService).startIngestion()
    }

    @Bean
    fun run() = CommandLineRunner {
        println("Application started")
//        val readText =
//            URL("https://www.alphavantage.co/query?symbol=amzn&function=TIME_SERIES_INTRADAY&interval=1min&outputsize=full&apikey=I8IYEDH87AFUTEHT").readText()
//
//        println("readText = ${readText}")
    }

    @Bean
    fun restTemplate(builder: RestTemplateBuilder): RestTemplate {
        return builder.build()
    }


}