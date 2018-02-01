package com.songzi.channel.service.dto;

import java.time.LocalDate;

public class SalesNumberDTO {

    private String salesNumberSum;

    private LocalDate date;

    public String getSalesNumberSum() {
        return salesNumberSum;
    }

    public void setSalesNumberSum(String salesNumberSum) {
        this.salesNumberSum = salesNumberSum;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
