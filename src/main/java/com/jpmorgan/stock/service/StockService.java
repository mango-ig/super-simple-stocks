package com.jpmorgan.stock.service;

import com.jpmorgan.stock.entity.Operation;
import com.jpmorgan.stock.entity.Stock;

import java.math.BigDecimal;

public interface StockService {

    int SCALE = 5;
    int ROUNDING = BigDecimal.ROUND_HALF_UP;

    BigDecimal calculateDividendYield(String stockSymbol);

    BigDecimal calculatePeRatio(String stockSymbol);

    BigDecimal calculateStockPrice(String stockSymbol);

    BigDecimal calculateIndex();

    void updateTickerPrice(String stockSymbol, BigDecimal tickerPrice);

    void recordTrade(String stockSymbol, int qntShares, Operation operation, BigDecimal price);

}
