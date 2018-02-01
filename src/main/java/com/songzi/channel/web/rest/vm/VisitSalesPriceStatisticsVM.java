package com.songzi.channel.web.rest.vm;

public class VisitSalesPriceStatisticsVM {


    private String saleTotal;

    //日环比
    private String d2d;

    //周环比
    private String w2w;

    //日均
    private String da;

    public String getSaleTotal() {
        return saleTotal;
    }

    public void setSaleTotal(String saleTotal) {
        this.saleTotal = saleTotal;
    }

    public String getD2d() {
        return d2d;
    }

    public void setD2d(String d2d) {
        this.d2d = d2d;
    }

    public String getW2w() {
        return w2w;
    }

    public void setW2w(String w2w) {
        this.w2w = w2w;
    }

    public String getDa() {
        return da;
    }

    public void setDa(String da) {
        this.da = da;
    }
}
