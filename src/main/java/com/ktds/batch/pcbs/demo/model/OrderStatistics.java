package com.ktds.batch.pcbs.demo.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
public class OrderStatistics {

    private String amount;

    private LocalDate date;

    @Builder
    private OrderStatistics(String amount, LocalDate date) {
        this.amount = amount;
        this.date = date;
    }
}
