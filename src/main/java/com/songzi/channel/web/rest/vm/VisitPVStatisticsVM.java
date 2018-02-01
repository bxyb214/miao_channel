package com.songzi.channel.web.rest.vm;

import com.songzi.channel.service.dto.PVDTO;

import java.util.List;

public class VisitPVStatisticsVM {

    //之前一周的访问量
    private List<PVDTO> pvs;

    //日均访问量
    private String pva;

    public List<PVDTO> getPvs() {
        return pvs;
    }

    public void setPvs(List<PVDTO> pvs) {
        this.pvs = pvs;
    }

    public String getPva() {
        return pva;
    }

    public void setPva(String pva) {
        this.pva = pva;
    }
}
