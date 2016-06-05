package com.jpmorgan.stock.entity;

import java.math.BigDecimal;
import java.util.LinkedList;

public class Stock {
    private String symbol;
    private StockType type;
    private BigDecimal lastDividend;
    private BigDecimal fixedDividend;
    private BigDecimal parValue;
    private final LinkedList<Trade> trades = new LinkedList<>();
    private BigDecimal tickerPrice;

    public Stock(String symbol, StockType type, BigDecimal lastDividend, BigDecimal fixedDividend, BigDecimal parValue, BigDecimal tickerPrice) {
        this.symbol = symbol;
        this.type = type;
        this.lastDividend = lastDividend;
        this.fixedDividend = fixedDividend;
        this.parValue = parValue;
        this.tickerPrice = tickerPrice;
    }

    public String getSymbol() {
        return symbol;
    }

    public StockType getType() {
        return type;
    }

    public BigDecimal getLastDividend() {
        return lastDividend;
    }

    public BigDecimal getFixedDividend() {
        return fixedDividend;
    }

    public BigDecimal getParValue() {
        return parValue;
    }

    public LinkedList<Trade> getTrades() {
        return trades;
    }

    public BigDecimal getTickerPrice() {
        return tickerPrice;
    }

    public void setLastDividend(BigDecimal lastDividend) {
        this.lastDividend = lastDividend;
    }

    public void setFixedDividend(BigDecimal fixedDividend) {
        this.fixedDividend = fixedDividend;
    }

    public void setParValue(BigDecimal parValue) {
        this.parValue = parValue;
    }

    public void setTickerPrice(BigDecimal tickerPrice) {
        this.tickerPrice = tickerPrice;
    }
}
