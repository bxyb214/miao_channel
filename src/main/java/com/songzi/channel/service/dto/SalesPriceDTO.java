package com.songzi.channel.service.dto;

import java.time.LocalDate;

public class SalesPriceDTO {

    private String salesPriceSum;

    private LocalDate date;

    public String getSalesPriceSum() {
        return salesPriceSum;
    }

    public void setSalesPriceSum(String salesPriceSum) {
        this.salesPriceSum = salesPriceSum;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
