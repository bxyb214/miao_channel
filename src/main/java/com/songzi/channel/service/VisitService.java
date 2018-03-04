package com.songzi.channel.service;

import com.songzi.channel.config.Constants;
import com.songzi.channel.domain.Channel;
import com.songzi.channel.domain.Product;
import com.songzi.channel.domain.Statistics;
import com.songzi.channel.domain.Visit;
import com.songzi.channel.domain.enumeration.StatisticsType;
import com.songzi.channel.repository.ChannelRepository;
import com.songzi.channel.repository.ProductRepository;
import com.songzi.channel.repository.StatisticsRepository;
import com.songzi.channel.repository.VisitRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


/**
 * Service for Visit.
 */
@Service
@Transactional
public class VisitService {

    private final Logger log = LoggerFactory.getLogger(JhiOrderService.class);

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    private final VisitRepository visitRepository;

    private final StatisticsRepository statisticsRepository;

    private final ProductService productService;

    public static Statistics uvTotalStat;

    public static Statistics uvDailyStat;

    public static Statistics pvTotalStat;

    public static Statistics pvDailyStat;

    public static Set<String> ipSet;

    private static LocalDate today;

    public VisitService(VisitRepository visitRepository,
                        StatisticsRepository statisticsRepository,
                        ProductService productService) {
        this.visitRepository = visitRepository;
        this.statisticsRepository = statisticsRepository;
        this.productService = productService;
        init();
    }

    private void init() {

        today = LocalDate.now();

        initUvStat();

        initPvStat();

        ipSet = visitRepository.findDistinctIpByDate(today);
    }



    public void count(String ip, String productCode, String channelCode) {

        if (!productService.getCodeSet().contains(productCode + "-" + channelCode)){
            return;
        }

        //1. pv
        pvTotalStat.setCount(pvTotalStat.getCount() + 1);
        pvDailyStat.setCount(pvDailyStat.getCount() + 1);

        //2. uv
        if (!ipSet.contains(ip)) {
            uvTotalStat.setCount(uvTotalStat.getCount() + 1);
            uvDailyStat.setCount(uvDailyStat.getCount() + 1);
            ipSet.add(ip);
        }

        addVisit(ip, productCode, channelCode);
    }

    @Async
    void addVisit(String ip, String productCode, String channelCode){
        Visit visit = new Visit();
        visit.setDate(LocalDate.now());
        visit.setChannelCode(channelCode);
        visit.setProductCode(productCode);
        visit.setIp(ip);
        visitRepository.save(visit);
    }

    /**
     * 每天重置PvDaily, UvDaily
     */
    @Scheduled(cron = "1 0 0 1/1 * *")
    public void resetDaily() {
        today = LocalDate.now();
        log.info("pv daily reset run");
        initPvStat();
        initUvStat();
        ipSet.clear();
    }

    /**
     * 每分钟指标刷至数据库
     */
    @Scheduled(cron = "0 0/1 * * * *")
    public void statisticsFlush2Db() {
        uvTotalStat.setUpdateDate(LocalDate.now());
        pvTotalStat.setUpdateDate(LocalDate.now());
        uvDailyStat.setUpdateDate(LocalDate.now());
        pvDailyStat.setUpdateDate(LocalDate.now());
        statisticsRepository.save(uvTotalStat);
        statisticsRepository.save(pvTotalStat);
        statisticsRepository.save(uvDailyStat);
        statisticsRepository.save(pvDailyStat);
    }

    private void initUvStat(){

        double count =  visitRepository.countDistinctByDate(today);

        //UV_DAILY
        uvDailyStat = statisticsRepository.findOneByTypeAndDate(StatisticsType.UV_DAILY, today);

        if (uvDailyStat == null) {
            uvDailyStat = new Statistics();
            uvDailyStat.setName(Constants.UV_DAILY);
            uvDailyStat.setType(StatisticsType.UV_DAILY);
            uvDailyStat.setDescription(Constants.UV_DAILY);
        }
        uvDailyStat.setUpdateDate(LocalDate.now());
        uvDailyStat.setDate(today);
        uvDailyStat.setCount(count);
        uvDailyStat = statisticsRepository.save(uvDailyStat);

        //UV_TOTAL
        uvTotalStat = statisticsRepository.findOneByTypeAndDate(StatisticsType.UV_TOTAL, today);
        double uvTotalStatYesterday = 0.0;
        Object uvTotalStatYesterdayObj = statisticsRepository.getCountByTypeAndDate(StatisticsType.UV_TOTAL, today.minusDays(1));
        if (uvTotalStatYesterdayObj != null){
            uvTotalStatYesterday = (double) uvTotalStatYesterdayObj;
        }
        if (uvTotalStat == null) {
            uvTotalStat = new Statistics();
            uvTotalStat.setName(Constants.UV_TOTAL);
            uvTotalStat.setType(StatisticsType.UV_TOTAL);
            uvTotalStat.setChannelCode(null);
            uvTotalStat.setProductCode(null);
            uvTotalStat.setDescription(Constants.UV_TOTAL);
        }
        uvTotalStat.setDate(today);
        uvTotalStat.setCount(count + uvTotalStatYesterday);
        uvTotalStat.setUpdateDate(LocalDate.now());
        uvTotalStat = statisticsRepository.save(uvTotalStat);
    }

    private void initPvStat(){

        double count =  visitRepository.countByDate(today);

        pvDailyStat = statisticsRepository.findOneByTypeAndDate(StatisticsType.PV_DAILY, today);
        if (pvDailyStat == null) {
            pvDailyStat = new Statistics();
            pvDailyStat.setName(Constants.PV_DAILY);
            pvDailyStat.setType(StatisticsType.PV_DAILY);
            pvDailyStat.setDescription(Constants.PV_DAILY);
        }
        pvDailyStat.setDate(today);
        pvDailyStat.setUpdateDate(LocalDate.now());
        pvDailyStat.setCount(count);
        pvDailyStat = statisticsRepository.save(pvDailyStat);


        pvTotalStat = statisticsRepository.findOneByTypeAndDate(StatisticsType.PV_TOTAL, today);
        double pvTotalStatYesterday = 0.0;
        Object pvTotalStatYesterdayObj = statisticsRepository.getCountByTypeAndDate(StatisticsType.PV_TOTAL, today.minusDays(1));
        if (pvTotalStatYesterdayObj != null){
            pvTotalStatYesterday = (double) pvTotalStatYesterdayObj;
        }
        if (pvTotalStat == null) {
            pvTotalStat = new Statistics();
            pvTotalStat.setName(Constants.PV_TOTAL);
            pvTotalStat.setType(StatisticsType.PV_TOTAL);
            pvTotalStat.setDescription(Constants.PV_TOTAL);

        }
        pvTotalStat.setCount(pvTotalStatYesterday + count);
        pvTotalStat.setDate(today);
        pvTotalStat.setUpdateDate(LocalDate.now());
        pvTotalStat = statisticsRepository.save(pvTotalStat);
    }

}
