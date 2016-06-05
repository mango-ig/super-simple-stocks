package com.jpmorgan.stock.service.impl;

import com.jpmorgan.stock.entity.Operation;
import com.jpmorgan.stock.entity.Stock;
import com.jpmorgan.stock.entity.Trade;
import com.jpmorgan.stock.repository.StocksRepository;
import com.jpmorgan.stock.service.ApplicationTimeService;
import com.jpmorgan.stock.service.StockService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Stream;

public class StockServiceImpl implements StockService {

    private StocksRepository stocksRepository;

    private ApplicationTimeService timeService;

    public StockServiceImpl(StocksRepository stocksRepository, ApplicationTimeService timeService) {
        this.stocksRepository = stocksRepository;
        this.timeService = timeService;
    }

    /**
     * calculate the dividend yield
     * @param stockSymbol - Stock symbol
     * @return Dividend Yield
     */
    @Override
    public BigDecimal calculateDividendYield(String stockSymbol) {
        Stock stock = stocksRepository.getStockById(stockSymbol);
        BigDecimal tickerPrice = stock.getTickerPrice();
        BigDecimal dividend = getDividend(stock);
        BigDecimal dividendYield = BigDecimal.ZERO;
        if (BigDecimal.ZERO.compareTo(tickerPrice) != 0) {
            dividendYield = dividend.divide(tickerPrice, SCALE, ROUNDING);
        }
        return dividendYield;
    }

    /**
     * calculate the P/E Ratio
     * @param stockSymbol - Stock symbol
     * @return P/E Ratio
     */
    @Override
    public BigDecimal calculatePeRatio(String stockSymbol) {
        Stock stock = stocksRepository.getStockById(stockSymbol);
        BigDecimal dividend = getDividend(stock);
        BigDecimal peRatio = BigDecimal.ZERO;
        if (BigDecimal.ZERO.compareTo(dividend) != 0) {
            peRatio = stock.getTickerPrice().divide(dividend, SCALE, ROUNDING);
        }
        return peRatio;
    }

    /**
     * Calculate Stock Price based on trades recorded in past 15 minutes
     * @param stockSymbol - Stock symbol
     * @return - Stock Price
     */
    public BigDecimal calculateStockPrice(String stockSymbol) {
        Stock stock = stocksRepository.getStockById(stockSymbol);
        Iterator<Trade> iterator = stock.getTrades().descendingIterator();
        LocalDateTime pointInTime = timeService.getApplicationTime().minusMinutes(15);
        int sumOfShares = 0;
        BigDecimal totalPrice = BigDecimal.ZERO;
        Trade trade;
        while (iterator.hasNext() && (trade = iterator.next()) != null
                && trade.getTimestamp().isAfter(pointInTime)) {
            sumOfShares += trade.getQuantityOfShares();
            totalPrice = totalPrice.add(trade.getPrice().multiply(BigDecimal.valueOf(trade.getQuantityOfShares())));
        }
        BigDecimal stockPrice = BigDecimal.ZERO;
        if (sumOfShares > 0) {
            stockPrice = totalPrice.divide(BigDecimal.valueOf(sumOfShares), SCALE, ROUNDING);
        }
        return stockPrice;
    }

    /**
     * Calculate the GBCE All Share Index using the geometric mean of prices for all stocks
     * @return GBCE All Share Index
     */
    public BigDecimal calculateIndex() {
        Collection<Stock> stocks = stocksRepository.getAllStocks();
        if (stocks == null || stocks.size() == 0)
            return BigDecimal.ZERO;
        Stream<BigDecimal> stream = stocks.stream().map(s -> calculateStockPrice(s.getSymbol()));
        Optional<BigDecimal> stocksPriceProduct = stream.reduce(BigDecimal::multiply);

        return BigDecimal.valueOf(Math.pow(stocksPriceProduct.orElse(BigDecimal.ZERO).doubleValue(), 1.0 / stocks.size())).setScale(SCALE, ROUNDING);
    }

    /**
     * Update stock ticker price
     * @param stockSymbol - Stock symbol
     * @param tickerPrice - new ticker price
     */
    @Override
    public void updateTickerPrice(String stockSymbol, BigDecimal tickerPrice) {
        Stock stock = stocksRepository.getStockById(stockSymbol);
        stock.setTickerPrice(tickerPrice);
    }

    /**
     * Return dividend according to stock type
     * @param stock - stock for dividend calculation
     * @return - dividend
     */
    private BigDecimal getDividend(Stock stock) {
        BigDecimal dividend;
        switch (stock.getType()) {
            case COMMON:
                dividend = stock.getLastDividend();
                break;
            case PREFERRED:
                dividend = stock.getFixedDividend().multiply(stock.getParValue());
                break;
            default:
                throw new RuntimeException("Not supported stock type: " + stock.getType());
        }
        return dividend;
    }

    /**
     * Record new trade operation for the stock
     * @param stockSymbol - Stock symbol
     * @param qntShares - quantity of shares
     * @param operation - BUY or SELL operation
     * @param price - share price
     */
    @Override
    public void recordTrade(String stockSymbol, int qntShares, Operation operation, BigDecimal price) {
        Stock stock = stocksRepository.getStockById(stockSymbol);
        LocalDateTime timestamp = timeService.getApplicationTime();
        stock.getTrades().add(new Trade(timestamp, qntShares, operation, price));
    }
}
