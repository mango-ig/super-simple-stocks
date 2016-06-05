package com.jpmorgan.stock;

import com.jpmorgan.stock.entity.Operation;
import com.jpmorgan.stock.entity.Stock;
import com.jpmorgan.stock.repository.StocksRepository;
import com.jpmorgan.stock.service.ApplicationTimeService;
import com.jpmorgan.stock.service.StockService;
import com.jpmorgan.stock.service.impl.ApplicationTimeServiceImpl;
import com.jpmorgan.stock.service.impl.StockServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Collection;

public class SimpleStockApplication {
    private static Logger logger = LoggerFactory.getLogger(SimpleStockApplication.class);

    public static void main(String[] args) throws Exception {
        StocksRepository stocksRepository = new StocksRepository();
        ApplicationTimeService timeService = new ApplicationTimeServiceImpl();
        StockService stockService = new StockServiceImpl(stocksRepository, timeService);
        Collection<Stock> allStocks = stocksRepository.getAllStocks();
        for (Stock stock : allStocks) {
            logger.info("Stock: {}", stock.getSymbol());
            stockService.recordTrade(stock.getSymbol(), 2, Operation.BUY, BigDecimal.valueOf(25));
            stockService.recordTrade(stock.getSymbol(), 3, Operation.SELL, BigDecimal.valueOf(30));
            stockService.recordTrade(stock.getSymbol(), 4, Operation.BUY, BigDecimal.valueOf(35));
            logger.info("{} trades recorded", stock.getTrades().size());
            BigDecimal dividendYield = stockService.calculateDividendYield(stock.getSymbol());
            logger.info("DividendYield = {}", dividendYield);
            BigDecimal peRatio = stockService.calculatePeRatio(stock.getSymbol());
            logger.info("P/E Ratio = {}", peRatio);
            BigDecimal stockPrice = stockService.calculateStockPrice(stock.getSymbol());
            logger.info("Stock Price = {}", stockPrice);
        }
        BigDecimal index = stockService.calculateIndex();
        logger.info("GBCE All Share Index = {}", index);

    }
}
