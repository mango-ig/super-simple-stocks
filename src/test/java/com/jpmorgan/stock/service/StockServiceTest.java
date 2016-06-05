package com.jpmorgan.stock.service;

import com.jpmorgan.stock.entity.Operation;
import com.jpmorgan.stock.entity.Stock;
import com.jpmorgan.stock.entity.Trade;
import com.jpmorgan.stock.repository.StocksRepository;
import com.jpmorgan.stock.service.impl.ApplicationTimeServiceImpl;
import com.jpmorgan.stock.service.impl.StockServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedList;

import static com.jpmorgan.stock.entity.StockType.COMMON;
import static com.jpmorgan.stock.entity.StockType.PREFERRED;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StockServiceTest {

    @Spy
    private ApplicationTimeServiceImpl timeService;

    @Mock
    private StocksRepository stocksRepository;

    @Spy
    @InjectMocks
    private StockServiceImpl stockService = new StockServiceImpl(null, null);

    @Test
    public void testCalculateDividendYieldForCommonStock() throws Exception {
        String stockSymbol = "ABC";
        Stock stock = new Stock(stockSymbol, COMMON, BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.TEN, new BigDecimal("3"));
        when(stocksRepository.getStockById(stockSymbol)).thenReturn(stock);
        assertThat(stockService.calculateDividendYield(stockSymbol), is(new BigDecimal("3.33333")));

        stock.setTickerPrice(BigDecimal.ZERO);
        assertThat(stockService.calculateDividendYield(stockSymbol), is(BigDecimal.ZERO));
    }

    @Test
    public void testCalculateDividendYieldForPreferredStock() throws Exception {
        String stockSymbol = "ABC";
        Stock stock = new Stock(stockSymbol, PREFERRED, BigDecimal.ONE, new BigDecimal("0.02"), new BigDecimal("1000"), new BigDecimal("4"));
        when(stocksRepository.getStockById(stockSymbol)).thenReturn(stock);
        BigDecimal dividendYield = stockService.calculateDividendYield(stockSymbol);
        assertThat(dividendYield, is(new BigDecimal("5.00000")));

        stock.setTickerPrice(BigDecimal.ZERO);
        assertThat(stockService.calculateDividendYield(stockSymbol), is(BigDecimal.ZERO));
    }

    @Test
    public void testCalculatePeRatio() throws Exception {
        String stockSymbol = "ABC";
        Stock stock = new Stock(stockSymbol, COMMON, BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.TEN, new BigDecimal("3"));
        when(stocksRepository.getStockById(stockSymbol)).thenReturn(stock);
        assertThat(stockService.calculatePeRatio(stockSymbol), is(new BigDecimal("0.30000")));

        when(stocksRepository.getStockById(stockSymbol))
                .thenReturn(new Stock(stockSymbol, COMMON, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.TEN, new BigDecimal("3")));
        assertThat(stockService.calculatePeRatio(stockSymbol), is(BigDecimal.ZERO));
    }

    @Test
    public void testCalculateStockPrice() throws Exception {
        String stockSymbol = "ABC";
        Stock stock = new Stock(stockSymbol, COMMON, BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.TEN, new BigDecimal("3"));
        when(stocksRepository.getStockById(stockSymbol)).thenReturn(stock);
        assertThat(stockService.calculateStockPrice(stockSymbol), is(BigDecimal.ZERO));

        stock.getTrades().add(new Trade(LocalDateTime.now().minusMinutes(16), 2, Operation.BUY, new BigDecimal("10")));
        stock.getTrades().add(new Trade(LocalDateTime.now().minusMinutes(14), 3, Operation.BUY, new BigDecimal("15")));
        stock.getTrades().add(new Trade(LocalDateTime.now().minusMinutes(13), 4, Operation.SELL, new BigDecimal("20")));
        assertThat(stockService.calculateStockPrice(stockSymbol), is(new BigDecimal("17.85714")));

    }

    @Test
    public void testCalculateShareIndex() throws Exception {
        assertThat(stockService.calculateIndex(), is(BigDecimal.ZERO));
        String stockSymbol = "ABC";
        Stock stock = new Stock(stockSymbol, COMMON, BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.TEN, new BigDecimal("3"));
        LinkedList<Stock> stocks = new LinkedList<>();
        Collections.addAll(stocks, stock, stock, stock);

        doReturn(stocks).when(stocksRepository).getAllStocks();
        doReturn(new BigDecimal("2")).doReturn(new BigDecimal("5")).doReturn(new BigDecimal("8")).when(stockService).calculateStockPrice(anyString());
        assertThat(stockService.calculateIndex(), is(new BigDecimal("4.30887")));

        doReturn(BigDecimal.ZERO).when(stockService).calculateStockPrice(anyString());
        assertThat(stockService.calculateIndex(), is(new BigDecimal("0.00000")));
    }

    @Test
    public void testRecordTrade() throws Exception {
        String stockSymbol = "ABC";
        Stock stock = spy(new Stock(stockSymbol, COMMON, BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.TEN, new BigDecimal("3")));
        when(stocksRepository.getStockById(stockSymbol)).thenReturn(stock);
        stockService.recordTrade(stockSymbol, 5, Operation.BUY, BigDecimal.TEN);
        verify(stock).getTrades();
        assertThat(stock.getTrades().size(), is(1));

    }
}