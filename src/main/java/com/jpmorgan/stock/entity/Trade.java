package com.jpmorgan.stock.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Trade {
    private LocalDateTime timestamp;
    private Integer quantityOfShares;
    private Operation operation;
    private BigDecimal price;

    public Trade(LocalDateTime timestamp, Integer quantityOfShares, Operation operation, BigDecimal price) {
        this.timestamp = timestamp;
        this.quantityOfShares = quantityOfShares;
        this.operation = operation;
        this.price = price;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Integer getQuantityOfShares() {
        return quantityOfShares;
    }

    public Operation getOperation() {
        return operation;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
