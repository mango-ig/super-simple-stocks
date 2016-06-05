package com.jpmorgan.stock.service.impl;

import com.jpmorgan.stock.service.ApplicationTimeService;

import java.time.LocalDateTime;

public class ApplicationTimeServiceImpl implements ApplicationTimeService {

    @Override
    public LocalDateTime getApplicationTime() {
        return LocalDateTime.now();
    }
}
