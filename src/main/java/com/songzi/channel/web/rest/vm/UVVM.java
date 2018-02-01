package com.songzi.channel.web.rest.vm;

import java.time.LocalDate;

public class UVVM {
    private String uvSum;
    private LocalDate date;

    public String getUvSum() {
        return uvSum;
    }

    public void setUvSum(String uvSum) {
        this.uvSum = uvSum;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
