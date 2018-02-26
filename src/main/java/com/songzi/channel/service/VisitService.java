package com.songzi.channel.service;

import com.songzi.channel.config.Constants;
import com.songzi.channel.domain.Persona;
import com.songzi.channel.domain.Statistics;
import com.songzi.channel.domain.User;
import com.songzi.channel.domain.Visit;
import com.songzi.channel.domain.enumeration.StatisticsType;
import com.songzi.channel.repository.ChannelRepository;
import com.songzi.channel.repository.PersonaRepository;
import com.songzi.channel.repository.StatisticsRepository;
import com.songzi.channel.repository.VisitRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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

    private static int uvTotal;

    private static int uvDaily;

    private static int pvTotal;

    private static int pvDaily;

    private static int pvMonthly;

    private static int pvYearly;

    private static Set<String> ipSet;

    private static Map<String, Integer> productUv;

    public VisitService(VisitRepository visitRepository, ChannelRepository channelRepository, StatisticsRepository statisticsRepository) {
        this.visitRepository = visitRepository;
        this.channelRepository = channelRepository;
        this.statisticsRepository = statisticsRepository;
        init();
    }

    private void init() {

        LocalDate today = LocalDate.now();

        Statistics uvTotalStat = statisticsRepository.findOneByType(StatisticsType.UV_TOTAL);
        if (uvTotalStat == null) {
            Statistics statistics = new Statistics();
            statistics.setName(Constants.TOTAL_UV);
            statistics.setCount(0);
            statistics.setType(StatisticsType.UV_TOTAL);
            statistics.setDate(null);
            statisticsRepository.save(statistics);
            uvTotal = 0;
        } else {
            uvTotal = Integer.valueOf(uvTotalStat.getCount());
        }

        Statistics pvDailyStat = statisticsRepository.findOneByTypeAndDate(StatisticsType.UV_DAILY, today);
        if (pvDailyStat == null) {
            Statistics statistics = new Statistics();
            statistics.setName(Constants.TOTAL_UV_DAILY);
            statistics.setCount(0);
            statistics.setType(StatisticsType.UV_DAILY);
            statistics.setDate(LocalDate.now());
            statisticsRepository.save(statistics);
            uvDaily = 0;
        } else {
            uvDaily = Integer.valueOf(pvDailyStat.getCount());

        }

        Statistics pvTotalStat = statisticsRepository.findOneByType(StatisticsType.PV_TOTAL);
        if (pvTotalStat == null) {
            Statistics statistics = new Statistics();
            statistics.setName(Constants.TOTAL_PV);
            statistics.setCount(0);
            statistics.setType(StatisticsType.PV_TOTAL);
            statistics.setDate(null);
            statisticsRepository.save(statistics);
            pvTotal = 0;
        } else {
            pvTotal = Integer.valueOf(pvTotalStat.getCount());

        }

        Statistics uvDailyStat = statisticsRepository.findOneByTypeAndDate(StatisticsType.PV_DAILY, today);
        if (uvDailyStat == null) {
            Statistics statistics = new Statistics();
            statistics.setName(Constants.TOTAL_PV_DAILY);
            statistics.setCount(0);
            statistics.setType(StatisticsType.PV_DAILY);
            statistics.setDate(LocalDate.now());
            statisticsRepository.save(statistics);
            pvDaily = 0;
        } else {
            pvDaily = Integer.valueOf(uvDailyStat.getCount());

        }

        Statistics pvMonthlyStat = statisticsRepository.findOneByTypeAndDate(StatisticsType.PV_MONTHLY, today.withDayOfMonth(today.lengthOfMonth()));
        if (pvMonthlyStat == null) {
            Statistics statistics = new Statistics();
            statistics.setName(Constants.TOTAL_PV_MONTHLY);
            statistics.setCount(0);
            statistics.setType(StatisticsType.PV_MONTHLY);
            statistics.setDate(LocalDate.now());
            statisticsRepository.save(statistics);
            pvMonthly = 0;
        } else {
            pvMonthly = Integer.valueOf(pvMonthlyStat.getCount());
        }

        Statistics pvYearlyStat = statisticsRepository.findOneByTypeAndDate(StatisticsType.PV_YEARLY, today.withDayOfYear(today.lengthOfYear()));
        if (pvYearlyStat == null) {
            Statistics statistics = new Statistics();
            statistics.setName(Constants.TOTAL_PV_YEARLY);
            statistics.setCount(0);
            statistics.setType(StatisticsType.PV_YEARLY);
            statistics.setDate(LocalDate.now());
            statisticsRepository.save(statistics);
            pvYearly = 0;
        } else {
            pvYearly = Integer.valueOf(pvYearlyStat.getCount());
        }


        ipSet = new HashSet();
        List<Visit> visits = visitRepository.findAll();
        for (Visit v : visits) {
            ipSet.add(v.getIp());
        }

        productUv = new HashMap<>();
        List<Statistics> statisticsList = statisticsRepository.findAllByType(StatisticsType.UV_PRODUCT_TOTAL);
        for (Statistics s : statisticsList) {
            productUv.put(s.getName(), s.getCount());
        }
    }


    public void count(String ip, String productId) {

        //1. pv
        pvTotal++;
        pvDaily++;
        pvMonthly++;
        pvYearly++;


        //2. uv
        if (!ipSet.contains(ip)) {
            uvTotal++;
            uvDaily++;
            ipSet.add(ip);

            int count = productUv.get(productId);
            productUv.put(productId, count + 1);
        }
    }


    @Scheduled(cron = "1 0 0 1/1 * *")
    public void resetDaily() {
        log.info("pv daily reset run");
        Statistics statistics = new Statistics();
        statistics.setName(Constants.TOTAL_PV_DAILY);
        statistics.setCount(0);
        statistics.setType(StatisticsType.PV_DAILY);
        statistics.setDate(LocalDate.now());
        statisticsRepository.save(statistics);
        pvDaily = 0;
        uvDaily = 0;
        ipSet.clear();

    }

    /*
    There are only 6 fields: second, minute, hour, day of month, month, day(s) of week
     */
    @Scheduled(cron = "1 0 0 * 1/1 *")
    public void resetMonthly() {
        LocalDate today = LocalDate.now();
        log.info("pv month reset run");
        Statistics statistics = new Statistics();
        statistics.setName(Constants.TOTAL_PV_MONTHLY);
        statistics.setCount(0);
        statistics.setType(StatisticsType.PV_MONTHLY);
        statistics.setDate(today.withDayOfMonth(today.lengthOfMonth()));
        statisticsRepository.save(statistics);
        pvMonthly = 0;

        if (today.getMonth().getValue() == 1){
            log.info("pv year reset run");
            statistics = new Statistics();
            statistics.setName(Constants.TOTAL_PV_YEARLY);
            statistics.setCount(0);
            statistics.setType(StatisticsType.PV_YEARLY);
            statistics.setDate(today.withDayOfYear(today.lengthOfYear()));
            statisticsRepository.save(statistics);
            pvYearly = 0;
        }
    }

    @Scheduled(cron = "0 0/5 * * * *")
    public void statisticsFlush2Db() {

        LocalDate today = LocalDate.now();

        Statistics uvTotalStat = statisticsRepository.findOneByType(StatisticsType.UV_TOTAL);
        if (uvTotalStat != null) {
            uvTotalStat.setCount(uvTotal);
            statisticsRepository.saveAndFlush(uvTotalStat);
        }

        Statistics pvTotalStat = statisticsRepository.findOneByType(StatisticsType.PV_TOTAL);
        if (pvTotalStat != null) {
            pvTotalStat.setCount(pvTotal);
            statisticsRepository.saveAndFlush(pvTotalStat);
        }

        Statistics uvDailyStat = statisticsRepository.findOneByTypeAndDate(StatisticsType.UV_DAILY, today);
        if (uvDailyStat != null) {
            uvDailyStat.setCount(uvDaily);
            statisticsRepository.saveAndFlush(uvDailyStat);
        }

        Statistics pvDailyStat = statisticsRepository.findOneByTypeAndDate(StatisticsType.PV_DAILY, today);
        if (pvDailyStat != null) {
            pvDailyStat.setCount(pvDaily);
            statisticsRepository.saveAndFlush(pvDailyStat);
        }

        Statistics pvMonthlyStat = statisticsRepository.findOneByTypeAndDate(StatisticsType.PV_MONTHLY, today.withDayOfMonth(today.lengthOfMonth()));
        if (pvMonthlyStat != null) {
            pvMonthlyStat.setCount(pvMonthly);
            statisticsRepository.saveAndFlush(pvMonthlyStat);
        }

        Statistics pvYearlyStat = statisticsRepository.findOneByTypeAndDate(StatisticsType.PV_YEARLY, today.withDayOfYear(today.lengthOfYear()));
        if (pvYearlyStat != null) {
            pvYearlyStat.setCount(pvYearly);
            statisticsRepository.saveAndFlush(pvYearlyStat);
        }
    }

    @Scheduled(cron = "* 0/10 * * * *")
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
        statisticsRepository.deleteAllByType(StatisticsType.UV_PRODUCT_TOTAL);
        Iterator<Map.Entry<String, Integer>> it = productUv.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Integer> entry = it.next();
            Statistics statistics = new Statistics();
            statistics.setName(entry.getKey());
            statistics.setType(StatisticsType.UV_PRODUCT_TOTAL);
            statistics.setCount(entry.getValue());
            statisticsRepository.save(statistics);
        }
    }

    public static int getUvTotal() {
        return uvTotal;
    }

    public static int getUvDaily() {
        return uvDaily;
    }

    public static int getPvTotal() {
        return pvTotal;
    }

    public static int getPvDaily() {
        return pvDaily;
    }

    public static int getPvMonthly() {
        return pvMonthly;
    }

    public static int getPvYearly() {
        return pvYearly;
    }

    public static Set<String> getIpSet() {
        return ipSet;
    }

    public static Map<String, Integer> getProductUv() {
        return productUv;
    }
}
