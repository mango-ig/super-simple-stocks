package com.jpmorgan.stock.entity;

public enum Operation {
    BUY(1), SELL(-1);

    private final int multiplier;

    Operation(int multiplier) {
        this.multiplier = multiplier;
    }
}
