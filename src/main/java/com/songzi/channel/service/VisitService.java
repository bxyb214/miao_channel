package com.songzi.channel.service;

import com.songzi.channel.config.Constants;
import com.songzi.channel.domain.Statistics;
import com.songzi.channel.domain.Visit;
import com.songzi.channel.domain.enumeration.StatisticsType;
import com.songzi.channel.repository.ChannelRepository;
import com.songzi.channel.repository.StatisticsRepository;
import com.songzi.channel.repository.VisitRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;


/**
 * Service for Visit.
 */
@Service
@Transactional
public class VisitService {


    private final Logger log = LoggerFactory.getLogger(JhiOrderService.class);

    private final VisitRepository visitRepository;
    private final ChannelRepository channelRepository;
    private final StatisticsRepository statisticsRepository;

    public static Statistics uvTotalStat;

    public static Statistics uvDailyStat;

    public static Statistics pvTotalStat;

    public static Statistics pvMonthlyStat;

    public static Statistics pvDailyStat;

    public static Statistics pvYearlyStat;

    public static Set<String> ipSet;

    public static Map<String, Statistics> productUvStats;

    public static Map<String, Statistics> channelPvStats;

    public static Map<String, Statistics> channelUvStats;

    private static LocalDate today;

    public VisitService(VisitRepository visitRepository, ChannelRepository channelRepository, StatisticsRepository statisticsRepository) {
        this.visitRepository = visitRepository;
        this.channelRepository = channelRepository;
        this.statisticsRepository = statisticsRepository;
        init();
    }

    private void init() {


        today = LocalDate.now();

        initUvTotalStat();

        initUvDailyStat();

        initPvTotalStat();

        initPvDailyStat();

        initPvMonthlyStat();

        initPvYearlyStat();


        ipSet = new HashSet();
        List<Visit> visits = visitRepository.findAll();
        for (Visit v : visits) {
            ipSet.add(v.getIp());
        }

        productUvStats = new HashMap<>();
        List<Statistics> productUvStatList = statisticsRepository.findAllByType(StatisticsType.UV_PRODUCT_TOTAL);
        for (Statistics s : productUvStatList) {
            productUvStats.put(s.getName(), s);
        }

        initChannelPvStats();
        initChannelUvStats();

    }




    public void count(String ip, String productId) {

        //1. pv
        pvTotalStat.setCount(pvTotalStat.getCount() + 1);
        pvDailyStat.setCount(pvDailyStat.getCount() + 1);
        pvMonthlyStat.setCount(pvMonthlyStat.getCount() + 1);
        pvYearlyStat.setCount(pvYearlyStat.getCount() + 1);

        //2. uv
        if (!ipSet.contains(ip)) {
            uvTotalStat.setCount(uvTotalStat.getCount() + 1);
            uvDailyStat.setCount(uvDailyStat.getCount() + 1);
            ipSet.add(ip);

            Statistics productUvStat = productUvStats.get(productId);
            productUvStat.setCount(productUvStat.getCount() + 1);
            productUvStats.put(productId, productUvStat);
        }
    }


    @Scheduled(cron = "1 0 0 1/1 * *")
    public void resetDaily() {
        today = LocalDate.now();
        log.info("pv daily reset run");
        initPvDailyStat();
        initUvDailyStat();
        ipSet.clear();

    }

    /*
    There are only 6 fields: second, minute, hour, day of month, month, day(s) of week
     */
    @Scheduled(cron = "1 0 0 * 1/1 *")
    public void resetMonthly() {
        today = LocalDate.now();
        log.info("pv month reset run");
        initPvMonthlyStat();

        if (today.getMonth().getValue() == 1){
            log.info("pv year reset run");
            initPvYearlyStat();
        }
    }

    @Scheduled(cron = "0 0/5 * * * *")
    public void statisticsFlush2Db() {

        today = LocalDate.now();

        Statistics uvTotalStat = statisticsRepository.findOneByType(StatisticsType.UV_TOTAL);
        if (uvTotalStat != null) {
            uvTotalStat.setCount(uvTotalStat.getCount());
            statisticsRepository.saveAndFlush(uvTotalStat);
        }

        Statistics pvTotalStat = statisticsRepository.findOneByType(StatisticsType.PV_TOTAL);
        if (pvTotalStat != null) {
            pvTotalStat.setCount(pvTotalStat.getCount());
            statisticsRepository.saveAndFlush(pvTotalStat);
        }

        Statistics uvDailyStat = statisticsRepository.findOneByTypeAndDate(StatisticsType.UV_DAILY, today);
        if (uvDailyStat != null) {
            uvDailyStat.setCount(uvDailyStat.getCount());
            statisticsRepository.saveAndFlush(uvDailyStat);
        }

        Statistics pvDailyStat = statisticsRepository.findOneByTypeAndDate(StatisticsType.PV_DAILY, today);
        if (pvDailyStat != null) {
            pvDailyStat.setCount(pvDailyStat.getCount());
            statisticsRepository.saveAndFlush(pvDailyStat);
        }

        Statistics pvMonthlyStat = statisticsRepository.findOneByTypeAndDate(StatisticsType.PV_MONTHLY, today.withDayOfMonth(today.lengthOfMonth()));
        if (pvMonthlyStat != null) {
            pvMonthlyStat.setCount(pvMonthlyStat.getCount());
            statisticsRepository.saveAndFlush(pvMonthlyStat);
        }

        Statistics pvYearlyStat = statisticsRepository.findOneByTypeAndDate(StatisticsType.PV_YEARLY, today.withDayOfYear(today.lengthOfYear()));
        if (pvYearlyStat != null) {
            pvYearlyStat.setCount(pvYearlyStat.getCount());
            statisticsRepository.saveAndFlush(pvYearlyStat);
        }


    }

    @Scheduled(cron = "0 0/10 * * * *")
    public void ipFlush2Db() {
        //ip
        visitRepository.deleteAll();
        for (String ip : ipSet) {
            Visit visit = new Visit();
            visit.setIp(ip);
            visit.setDate(LocalDate.now());
            visitRepository.save(visit);
        }

        //UV_PRODUCT_TOTAL
        Iterator<Map.Entry<String, Statistics>> itProductUv = productUvStats.entrySet().iterator();
        while (itProductUv.hasNext()) {
            Map.Entry<String, Statistics> entry = itProductUv.next();
            statisticsRepository.save(entry.getValue());
        }

        Iterator<Map.Entry<String, Statistics>> itChannelPv = channelPvStats.entrySet().iterator();
        while (itChannelPv.hasNext()) {
            Map.Entry<String, Statistics> entry = itChannelPv.next();
            statisticsRepository.save(entry.getValue());
        }

        Iterator<Map.Entry<String, Statistics>> itChannelUv = channelUvStats.entrySet().iterator();
        while (itChannelUv.hasNext()) {
            Map.Entry<String, Statistics> entry = itChannelUv.next();
            statisticsRepository.save(entry.getValue());
        }
    }

    private void initUvTotalStat() {
        uvTotalStat = statisticsRepository.findOneByType(StatisticsType.UV_TOTAL);
        if (uvTotalStat == null) {
            Statistics statistics = new Statistics();
            statistics.setName(Constants.TOTAL_UV);
            statistics.setCount(0.0);
            statistics.setType(StatisticsType.UV_TOTAL);
            statistics.setDate(null);
            statisticsRepository.save(statistics);

        }
    }

    private void initUvDailyStat(){
        pvDailyStat = statisticsRepository.findOneByTypeAndDate(StatisticsType.UV_DAILY, today);
        if (pvDailyStat == null) {
            Statistics statistics = new Statistics();
            statistics.setName(Constants.TOTAL_UV_DAILY);
            statistics.setCount(0.0);
            statistics.setType(StatisticsType.UV_DAILY);
            statistics.setDate(LocalDate.now());
            statisticsRepository.save(statistics);
        }
    }

    private void initPvTotalStat(){
        pvTotalStat = statisticsRepository.findOneByType(StatisticsType.PV_TOTAL);
        if (pvTotalStat == null) {
            Statistics statistics = new Statistics();
            statistics.setName(Constants.TOTAL_PV);
            statistics.setCount(0.0);
            statistics.setType(StatisticsType.PV_TOTAL);
            statistics.setDate(null);
            statisticsRepository.save(statistics);

        }
    }

    private void initPvDailyStat(){
        uvDailyStat = statisticsRepository.findOneByTypeAndDate(StatisticsType.PV_DAILY, today);
        if (uvDailyStat == null) {
            Statistics statistics = new Statistics();
            statistics.setName(Constants.TOTAL_PV_DAILY);
            statistics.setCount(0.0);
            statistics.setType(StatisticsType.PV_DAILY);
            statistics.setDate(LocalDate.now());
            statisticsRepository.save(statistics);
        }
    }

    private void initPvMonthlyStat(){
        pvMonthlyStat = statisticsRepository.findOneByTypeAndDate(StatisticsType.PV_MONTHLY, today.withDayOfMonth(today.lengthOfMonth()));
        if (pvMonthlyStat == null) {
            Statistics statistics = new Statistics();
            statistics.setName(Constants.TOTAL_PV_MONTHLY);
            statistics.setCount(0.0);
            statistics.setType(StatisticsType.PV_MONTHLY);
            statistics.setDate(LocalDate.now());
            statisticsRepository.save(statistics);
        }
    }

    private void initPvYearlyStat(){
        pvYearlyStat = statisticsRepository.findOneByTypeAndDate(StatisticsType.PV_YEARLY, today.withDayOfYear(today.lengthOfYear()));
        if (pvYearlyStat == null) {
            Statistics statistics = new Statistics();
            statistics.setName(Constants.TOTAL_PV_YEARLY);
            statistics.setCount(0.0);
            statistics.setType(StatisticsType.PV_YEARLY);
            statistics.setDate(LocalDate.now());
            statisticsRepository.save(statistics);
        }
    }

    private void initChannelPvStats() {
        channelPvStats = new HashMap<>();
        List<Statistics> channelPvStatList = statisticsRepository.findAllByTypeAndDate(StatisticsType.PV_CHANNEL_DAILY, today);
        for (Statistics s : channelPvStatList) {
            channelPvStats.put(s.getName(), s);
        }
    }

    private void initChannelUvStats() {
        channelUvStats = new HashMap<>();
        List<Statistics> channelUvStatList = statisticsRepository.findAllByTypeAndDate(StatisticsType.UV_CHANNEL_DAILY, today);
        for (Statistics s : channelUvStatList) {
            channelPvStats.put(s.getName(), s);
        }
    }
}
