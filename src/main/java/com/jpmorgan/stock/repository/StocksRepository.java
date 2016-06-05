package com.jpmorgan.stock.repository;

import com.jpmorgan.stock.entity.Stock;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import static com.jpmorgan.stock.entity.StockType.COMMON;
import static com.jpmorgan.stock.entity.StockType.PREFERRED;

public class StocksRepository {

    //initialization of application data in memory
    private final static Map<String, Stock> stocks =
            Arrays.asList(
                    new Stock("TEA", COMMON, BigDecimal.valueOf(0), null, BigDecimal.valueOf(100), BigDecimal.valueOf(20)),
                    new Stock("POP", COMMON, BigDecimal.valueOf(8), null, BigDecimal.valueOf(100), BigDecimal.valueOf(20)),
                    new Stock("ALE", COMMON, BigDecimal.valueOf(23), null, BigDecimal.valueOf(60), BigDecimal.valueOf(20)),
                    new Stock("GIN", PREFERRED, BigDecimal.valueOf(8), new BigDecimal("0.02"), BigDecimal.valueOf(100), BigDecimal.valueOf(20)),
                    new Stock("JOE", COMMON, BigDecimal.valueOf(13), null, BigDecimal.valueOf(250), BigDecimal.valueOf(20)))
                    .stream().collect(Collectors.toMap(Stock::getSymbol, o -> o));

    public Stock getStockById(String id) {
        return stocks.get(id);
    }

    public Collection<Stock> getAllStocks() {
        return stocks.values();
    }
}
