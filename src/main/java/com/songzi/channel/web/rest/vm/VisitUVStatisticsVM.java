package com.songzi.channel.web.rest.vm;

import java.util.List;

public class VisitUVStatisticsVM {

    //之前一周的独立访问量
    private List<UVVM> uvs;

    //日均独立访问量
    private String uva;

    public List<UVVM> getUvs() {
        return uvs;
    }

    public void setUvs(List<UVVM> uvs) {
        this.uvs = uvs;
    }

    public String getUva() {
        return uva;
    }

    public void setUva(String uva) {
        this.uva = uva;
    }
}
