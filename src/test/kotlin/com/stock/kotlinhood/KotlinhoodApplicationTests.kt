package com.stock.kotlinhood

import com.stock.kotlinhood.mapper.Quote
import com.stock.kotlinhood.mapper.QuoteMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.annotation.Transactional


@RunWith(SpringRunner::class)
@SpringBootTest
@Transactional
@Rollback(true)
class KotlinhoodApplicationTests {

    @Autowired
    private lateinit var quoteMapper: QuoteMapper

    @Test
    fun `given Quote table exists, when some quotes are inserted, should be inserted`() {
        val symbol = "abc"
        quoteMapper.insert(Quote(symbol, 120.0, 121.0))
        assertThat(quoteMapper.quotesBy(symbol)).hasSize(1)
    }

}
