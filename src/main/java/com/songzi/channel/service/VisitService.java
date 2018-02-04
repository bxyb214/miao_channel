package com.songzi.channel.service;

import com.songzi.channel.domain.Visit;
import com.songzi.channel.repository.ChannelRepository;
import com.songzi.channel.repository.VisitRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


/**
 * Service for Visit.
 */
@Service
@Transactional
public class VisitService {

    private final Logger log = LoggerFactory.getLogger(JhiOrderService.class);

    private final VisitRepository visitRepository;
    private final ChannelRepository channelRepository;

    public VisitService(VisitRepository visitRepository, ChannelRepository channelRepository) {
        this.visitRepository = visitRepository;
        this.channelRepository = channelRepository;
    }



//    /**
//     * Get the Visit.
//     *
//     * @return the list of entities
//     */
//    @Transactional(readOnly = true)
//    public List<Visit> findAll() {
//        log.debug("Request to get all Jhi_orders");
//        return visitRepository.findAll();
//    }
//
//    public StatisticsSalesTotalVM getSalesPriceStatistics() {
//        String saleTotal = null;
//        String d2d = null;
//        String w2w = null;
//        String da = null;
//
//        saleTotal = visitRepository.getSalePriceTotal() + "";
//
//        LocalDate today = LocalDate.now();
//        LocalDate yesterday = LocalDate.now().plusDays(-1);
//
//        double todaySaleTotal = visitRepository.findOneByDate(today).getSalesPrice();
//        double yesterdaySaleTotal = visitRepository.findOneByDate(yesterday).getSalesPrice();
//
//
//        if (yesterdaySaleTotal != 0.0){
//            d2d = todaySaleTotal/yesterdaySaleTotal + "";
//        }
//
//        //TODO w2w
//
//        //TODO da
//
//        StatisticsSalesTotalVM visitSalesTotalVM = new StatisticsSalesTotalVM();
//        visitSalesTotalVM.setD2d(d2d);
//        visitSalesTotalVM.setW2w(w2w);
//        visitSalesTotalVM.setDa(da);
//
//        return visitSalesTotalVM;
//    }
//
//
//    public VisitPVStatisticsVM getPVStatistics() {
//
//        List<PVDTO> pvs = null;
//        String pva = null;
//
//        LocalDate dayLastWeek = LocalDate.now().plusDays(-7);
//
//        List<Visit> visits = visitRepository.findByDateAFTER(dayLastWeek);
//
//        for (Visit visit : visits){
//            PVDTO PVDTO = new PVDTO();
//            PVDTO.setPvSum(visit.getPv() + "");
//            PVDTO.setDate(visit.getDate());
//            pvs.add(PVDTO);
//        }
//
//        VisitPVStatisticsVM visitPVStatisticsVM = new VisitPVStatisticsVM();
//        visitPVStatisticsVM.setPvs(pvs);
//        visitPVStatisticsVM.setPva(pva);
//
//        return visitPVStatisticsVM;
//    }
//
//
//    public VisitUVStatisticsVM getUVStatistics() {
//        List<UVVM> uvs = null;
//        String uva = null;
//
//        LocalDate dayLastWeek = LocalDate.now().plusDays(-7);
//
//        List<Visit> visits = visitRepository.findByDateAFTER(dayLastWeek);
//
//        for (Visit visit : visits){
//            UVVM uvvm = new UVVM();
//            uvvm.setUvSum(visit.getUv() + "");
//            uvvm.setDate(visit.getDate());
//            uvs.add(uvvm);
//        }
//
//        VisitUVStatisticsVM visitUVStatisticsVM = new VisitUVStatisticsVM();
//        visitUVStatisticsVM.setUvs(uvs);
//        visitUVStatisticsVM.setUva(uva);
//
//        return visitUVStatisticsVM;
//    }
//
//    public VisitSalesNumberStatisticsVM getSalesNumberStatistics() {
//
//        String payNumberStr = null;
//        String pcStr = null;
//
//        int payNumber = visitRepository.getSaleNumberTotal();
//        int pvTatal = visitRepository.getPvTotal();
//
//        if(pvTatal != 0){
//            pcStr = payNumber / pvTatal + "";
//        }
//        payNumberStr = payNumber + "";
//
//        VisitSalesNumberStatisticsVM visitSalesNumberStatisticsVM = new VisitSalesNumberStatisticsVM();
//        visitSalesNumberStatisticsVM.setPayNumber(payNumberStr);
//        visitSalesNumberStatisticsVM.setPc(pcStr);
//        return visitSalesNumberStatisticsVM;
//    }
//
//    public List<ChannelSalesPriceDTO> getChannelsOrderBySalesPrice(LocalDate startDate, LocalDate endDate) {
//
//        List<ChannelSalesPriceDTO> channelSalesPriceDTOS = visitRepository.getChannelsOrderBySalesPrice(startDate, endDate);
//
//        for (ChannelSalesPriceDTO channelSalesPriceDTO : channelSalesPriceDTOS){
//            //这个时候还是id
//            String channelName = channelRepository.findOneById(channelSalesPriceDTO.getChannelName()).getName();
//            channelSalesPriceDTO.setChannelName(channelName);
//        }
//        return channelSalesPriceDTOS;
//    }

}
