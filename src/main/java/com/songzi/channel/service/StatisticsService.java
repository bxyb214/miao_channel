package com.songzi.channel.service;

import com.songzi.channel.config.Constants;
import com.songzi.channel.domain.*;
import com.songzi.channel.domain.enumeration.OrderStatus;
import com.songzi.channel.domain.enumeration.StatisticsType;
import com.songzi.channel.repository.*;
import com.songzi.channel.service.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Service for Statistics.
 */
@Service
@Transactional
public class StatisticsService {

    private final Logger log = LoggerFactory.getLogger(JhiOrderService.class);

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    private final StatisticsRepository statisticsRepository;

    private final ProductStatisticsRepository productStatisticsRepository;

    private final ChannelStatisticsRepository channelStatisticsRepository;

    private final JhiOrderRepository orderRepository;

    private final UserService userService;

    private final ChannelRepository channelRepository;

    private final ProductRepository productRepository;

    public StatisticsService(StatisticsRepository statisticsRepository,
                             ProductStatisticsRepository productStatisticsRepository,
                             ChannelStatisticsRepository channelStatisticsRepository,
                             UserService userService,
                             ChannelRepository channelRepository,
                             ProductRepository productRepository,
                             JhiOrderRepository orderRepository){
        this.statisticsRepository = statisticsRepository;
        this.productStatisticsRepository = productStatisticsRepository;
        this.channelStatisticsRepository = channelStatisticsRepository;
        this.userService = userService;
        this.productRepository = productRepository;
        this.channelRepository = channelRepository;
        this.orderRepository = orderRepository;
    }

    public List<Statistics> getProductSalesPriceStatistics(){
        List<Statistics> statisticsList = statisticsRepository.findAllByTypeAndChannelCodeOrderByCountDesc(StatisticsType.SALES_PRODUCT_CHANNEL_TOTAL, "0");
        List<Statistics> result = new ArrayList<>();

        int i = 0;
        int count = 0;
        for (Statistics s : statisticsList){
            if(i < 5){
                i ++;
                result.add(s);
            }else{
                count += Double.valueOf(s.getCount());
            }
        }

        if (count != 0){
            Statistics statistics = new Statistics();
            statistics.setName("其他");
            statistics.setCount((double)count);
            result.add(statistics);
        }
        return result;
    }

    public Page<ProductStatistics> getProductSalesNumberStatistics(Pageable pageable) {
        Page<ProductStatistics> page =  productStatisticsRepository.findAll(pageable);
        return page;

    }

    public List<Statistics> getSalesStatistics(LocalDate startDate, LocalDate endDate){

        int count = 0;
        long years = ChronoUnit.YEARS.between(startDate, endDate);
        if(years > 1) {
            List<Statistics> statistics =  statisticsRepository.findAllByTypeAndDateBetweenOrderByDateAsc(StatisticsType.SALES_YEARLY, startDate, endDate);
            List<Statistics> autoCompleStatistics = new ArrayList<>();
            LocalDate date = startDate.withDayOfYear(startDate.lengthOfYear());

            count = 0;
            for (int i = 0; i < years; i++) {
                if (count < statistics.size() && date.equals(statistics.get(count).getDate())){
                        autoCompleStatistics.add(statistics.get(count));
                        count++;
                }else {
                    Statistics compleStat = new Statistics();
                    compleStat.setCount(0.0);
                    compleStat.setDate(date);
                    compleStat.setType(StatisticsType.SALES_YEARLY);
                    autoCompleStatistics.add(compleStat);
                }
                date = date.plusYears(1);
            }
            return autoCompleStatistics;
        }
        long months = ChronoUnit.MONTHS.between(startDate, endDate);
        if(months > 1) {
            List<Statistics> statistics = statisticsRepository.findAllByTypeAndDateBetweenOrderByDateAsc(StatisticsType.SALES_MONTHLY, startDate, endDate);
            List<Statistics> autoCompleStatistics = new ArrayList<>();
            LocalDate date = startDate.withDayOfMonth(startDate.lengthOfMonth());

            count = 0;
            for (int i = 0; i < months; i++) {
                if (count < statistics.size() && date.equals(statistics.get(count).getDate())){
                    autoCompleStatistics.add(statistics.get(count));
                    count++;
                }else {
                    Statistics compleStat = new Statistics();
                    compleStat.setCount(0.0);
                    compleStat.setDate(date);
                    compleStat.setType(StatisticsType.SALES_MONTHLY);
                    autoCompleStatistics.add(compleStat);
                }
                date = date.plusMonths(1);
                date = date.withDayOfMonth(date.lengthOfMonth());
            }
            return autoCompleStatistics;
        }

        long days = ChronoUnit.DAYS.between(startDate, endDate);
        List<Statistics> statistics = statisticsRepository.findAllByTypeAndDateBetweenOrderByDateAsc(StatisticsType.SALES_DAILY, startDate, endDate);
        List<Statistics> autoCompleStatistics = new ArrayList<>();
        LocalDate date = startDate;
        count = 0;
        for (int i = 0; i <= days; i++) {
            if (count < statistics.size() && date.equals(statistics.get(count).getDate())){
                autoCompleStatistics.add(statistics.get(count));
                count++;
            }else {
                Statistics compleStat = new Statistics();
                compleStat.setCount(0.0);
                compleStat.setDate(date);
                compleStat.setType(StatisticsType.SALES_DAILY);

                autoCompleStatistics.add(compleStat);
            }
            date = date.plusDays(1);
        }
        return autoCompleStatistics;
    }

    public List<Statistics> getPVStatistics(LocalDate startDate, LocalDate endDate){
        int count = 0;
        long years = ChronoUnit.YEARS.between(startDate, endDate);
        if(years > 1) {
            List<Statistics> statistics =  statisticsRepository.findAllByTypeAndDateBetweenOrderByDateAsc(StatisticsType.PV_YEARLY, startDate, endDate);
            List<Statistics> autoCompleStatistics = new ArrayList<>();
            LocalDate date = startDate.withDayOfYear(startDate.lengthOfYear());

            count = 0;
            for (int i = 0; i < years; i++) {
                if (count < statistics.size() && date.equals(statistics.get(count).getDate())){
                    autoCompleStatistics.add(statistics.get(count));
                    count++;
                }else {
                    Statistics compleStat = new Statistics();
                    compleStat.setCount(0.0);
                    compleStat.setDate(date);
                    compleStat.setType(StatisticsType.PV_YEARLY);
                    autoCompleStatistics.add(compleStat);
                }
                date = date.plusYears(1);
            }
            return autoCompleStatistics;
        }
        long months = ChronoUnit.MONTHS.between(startDate, endDate);
        if(months > 1) {
            List<Statistics> statistics = statisticsRepository.findAllByTypeAndDateBetweenOrderByDateAsc(StatisticsType.PV_MONTHLY, startDate, endDate);
            List<Statistics> autoCompleStatistics = new ArrayList<>();
            LocalDate date = startDate.withDayOfMonth(startDate.lengthOfMonth());

            count = 0;
            for (int i = 0; i < months; i++) {
                if (count < statistics.size() && date.equals(statistics.get(count).getDate())){
                    autoCompleStatistics.add(statistics.get(count));
                    count++;
                }else {
                    Statistics compleStat = new Statistics();
                    compleStat.setCount(0.0);
                    compleStat.setDate(date);
                    compleStat.setType(StatisticsType.PV_MONTHLY);
                    autoCompleStatistics.add(compleStat);
                }
                date = date.plusMonths(1);
                date = date.withDayOfMonth(date.lengthOfMonth());
            }
            return autoCompleStatistics;
        }

        long days = ChronoUnit.DAYS.between(startDate, endDate);
        List<Statistics> statistics = statisticsRepository.findAllByTypeAndDateBetweenOrderByDateAsc(StatisticsType.PV_DAILY, startDate, endDate);
        List<Statistics> autoCompleStatistics = new ArrayList<>();
        LocalDate date = startDate;
        count = 0;
        for (int i = 0; i <= days; i++) {
            if (count < statistics.size() && date.equals(statistics.get(count).getDate())){
                autoCompleStatistics.add(statistics.get(count));
                count++;
            }else {
                Statistics compleStat = new Statistics();
                compleStat.setCount(0.0);
                compleStat.setDate(date);
                compleStat.setType(StatisticsType.PV_DAILY);

                autoCompleStatistics.add(compleStat);
            }
            date = date.plusDays(1);
        }
        return autoCompleStatistics;
    }

    public List<Statistics> getPVTotalStatistics() {

        Statistics totalPv = VisitService.pvTotalStat;
        Statistics pvDaily = VisitService.pvDailyStat;
        List<Statistics> statistics = new ArrayList<>();
        statistics.add(totalPv);
        statistics.add(pvDaily);
        return statistics;
    }

    public List<Statistics> getUVTotalStatistics() {
        Statistics totalUv = VisitService.uvTotalStat;
        Statistics uvDaily = VisitService.uvDailyStat;
        List<Statistics> statistics = new ArrayList<>();
        statistics.add(totalUv);
        statistics.add(uvDaily);
        return statistics;
    }

    public List<Statistics> getSalesTotalStatistics() {

        LocalDate today = LocalDate.now();

        //今日销售额
        Double salesToday = orderRepository.sumPriceByStatusAndOrderDateBetween(OrderStatus.已支付.toString(), DateUtil.getStartOfDay(today), DateUtil.getEndOfDay(today));
        Statistics salesDaily = new Statistics();
        salesDaily.setCount(salesToday);
        salesDaily.setDate(today);
        salesDaily.setDescription(Constants.SALES_DAILY);
        salesDaily.setType(StatisticsType.SALES_DAILY);
        salesDaily.setName(Constants.SALES_DAILY);
        log.debug("今日销售额 = " + salesToday);

        //昨天统计的销售总量
        double salesTotalYesterday = 0.0;
        Object salesTotalYesterdayObj = statisticsRepository.getCountByTypeAndDate(StatisticsType.SALES_TOTAL.toString(), today.minusDays(1));
        if (salesTotalYesterdayObj != null){
            salesTotalYesterday = (double) salesTotalYesterdayObj;
        }
        log.debug("昨天统计的销售总量 = " + (salesTotalYesterday));

        //昨天日销售量
        double salesDailyYesterday = 0.0;
        Object salesDailyYesterdayObj = statisticsRepository.getCountByTypeAndDate(StatisticsType.SALES_DAILY.toString(), today.minusDays(1));
        if (salesDailyYesterdayObj != null){
            salesDailyYesterday = (double)salesDailyYesterdayObj;
        }

        log.debug("昨天日销售量 = " + (salesDailyYesterday));

        //销售总额
        Statistics salesTotal = new Statistics();
        salesTotal.setCount(salesTotalYesterday + salesToday);
        salesTotal.setDate(today);
        salesTotal.setDescription(Constants.SALES_TOTAL);
        salesTotal.setName(Constants.SALES_TOTAL);
        salesTotal.setType(StatisticsType.SALES_TOTAL);
        log.debug("销售总额 = " + (salesTotalYesterday + salesToday));


        //日增量
        Statistics salesTotalD2d = new Statistics();
        salesTotalD2d.setCount(salesToday - salesDailyYesterday);
        salesTotalD2d.setType(StatisticsType.SALES_TOTAL_D2D);
        salesTotalD2d.setName(Constants.SALES_TOTAL_D2D);
        salesTotalD2d.setDescription("昨天销售额-前天销售额");

        log.debug("日增量 = " + (salesToday - salesDailyYesterday));

        List<Statistics> statistics = new ArrayList<>();
        statistics.add(salesTotal);
        statistics.add(salesTotalD2d);
        statistics.add(salesDaily);
        return statistics;
    }

    public List<Statistics> getPayTotalStatistics() {

        LocalDate today = LocalDate.now();

        double payToday = orderRepository.countByStatusAndOrderDateBetween(OrderStatus.已支付, DateUtil.getStartOfDay(today), DateUtil.getEndOfDay(today));
        Statistics payStatisticsDaily = new Statistics();
        payStatisticsDaily.setCount(payToday);
        payStatisticsDaily.setDate(today);
        payStatisticsDaily.setName(Constants.PAY_DAILY);
        payStatisticsDaily.setType(StatisticsType.PAY_DAILY);
        payStatisticsDaily.setDescription(Constants.PAY_DAILY);

        //昨日总体的支付数
        double payTotalYesterday = 0.0;
        Object payTotalYesterdayObj = statisticsRepository.getCountByTypeAndDate(StatisticsType.PAY_TOTAL.toString(), today);
        if (payTotalYesterdayObj != null){
            payTotalYesterday = (double)payTotalYesterdayObj;
        }
        Statistics payTotal = new Statistics();
        payTotal.setDate(today);
        payTotal.setDescription(Constants.PAY_TOTAL);
        payTotal.setName(Constants.PAY_TOTAL);
        payTotal.setType(StatisticsType.PAY_TOTAL);
        payTotal.setCount(payTotalYesterday + payToday);

        List<Statistics> statistics = new ArrayList<>();
        statistics.add(payTotal);
        statistics.add(payStatisticsDaily);
        return statistics;
    }

    public Page<ChannelStatistics> getChannelStatistics(Pageable pageable) {
        long channelId = 0L;

        Channel channel= userService.getCurrentUserChannel();
        if (channel != null){
            channelId = channel.getId();
        }
        return channelStatisticsRepository.findAllByChannelId(channelId, pageable);
    }

    public List<Statistics> getChannelSalesStatistics() {
        return statisticsRepository.findAllByTypeAndProductCode(StatisticsType.SALES_PRODUCT_CHANNEL_TOTAL, "0");
    }
}
