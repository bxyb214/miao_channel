package com.songzi.channel.service.dto;

import java.time.LocalDate;

public class PVDTO {
    private String pvSum;
    private LocalDate date;

    public String getPvSum() {
        return pvSum;
    }

    public void setPvSum(String pvSum) {
        this.pvSum = pvSum;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
