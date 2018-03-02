package com.songzi.channel.service;


import com.songzi.channel.config.Constants;
import com.songzi.channel.domain.*;
import com.songzi.channel.domain.enumeration.StatisticsType;
import com.songzi.channel.domain.enumeration.Status;
import com.songzi.channel.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Service for Visit.
 */
@Service
@Transactional
public class StatisticsService {

    private final Logger log = LoggerFactory.getLogger(JhiOrderService.class);

    private final StatisticsRepository statisticsRepository;

    private final ProductStatisticsRepository productStatisticsRepository;

    private final ChannelStatisticsRepository channelStatisticsRepository;

    private final ChannelRepository channelRepository;

    private final JhiOrderRepository orderRepository;

    public StatisticsService(StatisticsRepository statisticsRepository,
                             ProductStatisticsRepository productStatisticsRepository,
                             ChannelStatisticsRepository channelStatisticsRepository,
                             ChannelRepository channelRepository,
                             JhiOrderRepository orderRepository){
        this.statisticsRepository = statisticsRepository;
        this.productStatisticsRepository = productStatisticsRepository;
        this.channelStatisticsRepository = channelStatisticsRepository;
        this.channelRepository = channelRepository;
        this.orderRepository = orderRepository;
    }

    public List<Statistics> getProductSalesPriceStatistics(){
        List<Statistics> statisticsList = statisticsRepository.findAllByTypeOrderByCountAsc(StatisticsType.PRODUCT_SALES);
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
                date.plusYears(1);
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
                date.plusYears(1);
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
        Statistics salesTotal = statisticsRepository.findOneByType(StatisticsType.SALES_TOTAL);
        Statistics salesTotalD2d = statisticsRepository.findOneByType(StatisticsType.SALES_TOTAL_D2D);
        Statistics salesDaily = statisticsRepository.findOneByTypeAndDate(StatisticsType.SALES_DAILY, LocalDate.now());

        List<Statistics> statistics = new ArrayList<>();
        statistics.add(salesTotal);
        statistics.add(salesTotalD2d);
        statistics.add(salesDaily);
        return statistics;
    }

    public List<Statistics> getPayTotalStatistics() {
        Statistics payTotal = statisticsRepository.findOneByType(StatisticsType.PAY_TOTAL);
        Statistics payTotalConversion = statisticsRepository.findOneByType(StatisticsType.PAY_TOTAL_CONVERSION);
        List<Statistics> statistics = new ArrayList<>();
        statistics.add(payTotal);
        statistics.add(payTotalConversion);
        return statistics;
    }

    public Page<ChannelStatistics> getChannelStatistics(Pageable pageable) {
        return channelStatisticsRepository.findAllByChannelId(1L, pageable);
    }


    public void createProductStatistics(Product product) {
        LocalDate today = LocalDate.now();
        Statistics statistics = new Statistics();
        statistics.setCount(0.0);
        statistics.setName(product.getName());
        statistics.setType(StatisticsType.PRODUCT_SALES_MONTHLY);
        statistics.setDate(today.withDayOfMonth(today.lengthOfMonth()));
        statisticsRepository.save(statistics);

        statistics = new Statistics();
        statistics.setCount(0.0);
        statistics.setName(product.getName());
        statistics.setType(StatisticsType.PRODUCT_SALES);
        statisticsRepository.save(statistics);

        statistics = new Statistics();
        statistics.setCount(0.0);
        statistics.setName(product.getName());
        statistics.setType(StatisticsType.PRODUCT_CONVERSION);
        statisticsRepository.save(statistics);

        statistics = new Statistics();
        statistics.setCount(0.0);
        statistics.setName(product.getName());
        statistics.setType(StatisticsType.UV_PRODUCT_TOTAL);
        statisticsRepository.save(statistics);

        ProductStatistics ps = new ProductStatistics();
        ps.setCount(0);
        ps.setName(product.getName());
        ps.setM2m(0);
        productStatisticsRepository.save(ps);

        statistics = new Statistics();
        statistics.setCount(0.0);
        statistics.setName(product.getName());
        statistics.setType(StatisticsType.UV_PRODUCT_TOTAL);
        statistics.setDate(today);
        statistics = statisticsRepository.save(statistics);
        VisitService.productUvStats.put(product.getName(), statistics);

    }

    public void createChannelStatistics(Channel c) {
        LocalDate today = LocalDate.now();
        Statistics channelStat = new Statistics();
        channelStat.setType(StatisticsType.CHANNEL_SALES);
        channelStat.setCount(0.0);
        channelStat.setName(c.getName());
        statisticsRepository.save(channelStat);

        channelStat = new Statistics();
        channelStat.setType(StatisticsType.PV_CHANNEL_DAILY);
        channelStat.setCount(0.0);
        channelStat.setName(c.getName());
        channelStat.setDate(today);
        channelStat = statisticsRepository.save(channelStat);
        VisitService.channelPvStats.put(c.getName(), channelStat);

        channelStat = new Statistics();
        channelStat.setType(StatisticsType.UV_CHANNEL_DAILY);
        channelStat.setCount(0.0);
        channelStat.setName(c.getName());
        channelStat.setDate(today);
        channelStat = statisticsRepository.save(channelStat);
        VisitService.channelUvStats.put(c.getName(), channelStat);
    }


    @Scheduled(cron = "1 0 0 1/1 * *")
    public void channelStatisticsDaily() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        log.info("channelStatisticsDaily run");

        List<Channel> channelList = channelRepository.findAllByStatus(Status.NORMAL);

        for (Channel c : channelList){
            ChannelStatistics channelStatistics = new ChannelStatistics();

            int pv = statisticsRepository.getCountByTypeAndNameAndDate(StatisticsType.PV_CHANNEL_DAILY, c.getName(), yesterday);
            channelStatistics.setPv(pv);

            int uv = statisticsRepository.getCountByTypeAndNameAndDate(StatisticsType.UV_CHANNEL_DAILY, c.getName(), yesterday);
            channelStatistics.setUv(uv);

            int orderNumber = orderRepository.getCountByChannelIdAndDate(c.getId(), yesterday);
            channelStatistics.setOrderNumber(orderNumber);

            int orderRate = orderNumber * 100 / uv * 100;
            channelStatistics.setOrderRate(orderRate);

            int payNumber = orderRepository.getCountByChannelIdAndDateAndStatus(c.getId(), yesterday, Status.NORMAL);
            channelStatistics.setPayNumber(payNumber);

            int payConversion = payNumber * 100 / uv * 100;
            channelStatistics.setPayConversion(payConversion);

            int salePrice = orderRepository.getPriceByChannelIdAndDateAndStatus(c.getId(), yesterday, Status.NORMAL);
            channelStatistics.setSalePrice(salePrice);

            int proportionPrice = orderRepository.getProportionPriceByChannelIdAndDateAndStatus(c.getId(), yesterday, Status.NORMAL);
            channelStatistics.setProportionPrice(proportionPrice);

            int uvOutput = salePrice * 100 / uv * 100;
            channelStatistics.setUvOutput(uvOutput);
            channelStatistics.setDate(yesterday);

            channelStatisticsRepository.save(channelStatistics);
        }
    }

}
