package com.stock.kotlinhood

import com.stock.kotlinhood.mapper.StockMapper
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.apache.ibatis.session.SqlSessionFactory
import org.mybatis.spring.SqlSessionFactoryBean
import org.mybatis.spring.SqlSessionTemplate
import org.mybatis.spring.annotation.MapperScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource


@Configuration
@MapperScan(basePackages = ["com.mangal.kotlinhood.mapper"])
open class AppConfig {

    @Bean
    fun dataSource(): DataSource {
        val hikariConfig = HikariConfig()
        hikariConfig.jdbcUrl = "jdbc:postgresql://localhost:15432/db_metamorphosis?binaryTransfer=true"
        hikariConfig.username = "postgres"
        hikariConfig.password = "postgres"
        hikariConfig.maximumPoolSize = 10
        hikariConfig.connectionTimeout = 30_1000
        return HikariDataSource(hikariConfig)
    }

    @Bean
    fun sqlSessionFactory(): SqlSessionFactory? {
        val sessionFactory = SqlSessionFactoryBean()
        sessionFactory.setDataSource(dataSource())
        return sessionFactory.`object`
    }

    @Bean
    fun stockRepo(): StockMapper {
        val sessionTemplate = SqlSessionTemplate(sqlSessionFactory())
        sessionTemplate.configuration.addMapper(StockMapper::class.java)
        return sessionTemplate.getMapper<StockMapper>(StockMapper::class.java)
    }
//
//    @Bean
//    fun stockMapper(): MapperFactoryBean<StockMapper> {
//        val factoryBean = MapperFactoryBean<StockMapper>(StockMapper::class.java)
//        factoryBean.setSqlSessionFactory(sqlSessionFactory())
//        return factoryBean
//    }

}