package com.songzi.channel.service;


import com.songzi.channel.domain.Channel;
import com.songzi.channel.domain.Product;
import com.songzi.channel.domain.ProductStatistics;
import com.songzi.channel.domain.Statistics;
import com.songzi.channel.domain.enumeration.StatisticsType;
import com.songzi.channel.repository.ProductStatisticsRepository;
import com.songzi.channel.repository.StatisticsRepository;
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

    public StatisticsService(StatisticsRepository statisticsRepository, ProductStatisticsRepository productStatisticsRepository){
        this.statisticsRepository = statisticsRepository;
        this.productStatisticsRepository = productStatisticsRepository;
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
                count += Integer.valueOf(s.getCount());
            }
        }

        if (count != 0){
            Statistics statistics = new Statistics();
            statistics.setName("其他");
            statistics.setCount(count);
            result.add(statistics);
        }
        return result;
    }

    public Page<ProductStatistics> getProductSalesNumberStatistics(Pageable pageable) {
        Page<ProductStatistics> page =  productStatisticsRepository.findAll(pageable);
        return page;

    }

    public List<Statistics> getSalesStatistics(LocalDate startDate, LocalDate endDate){
        long years = ChronoUnit.YEARS.between(startDate, endDate);
        if(years > 1) {
            return  statisticsRepository.findAllByTypeAndDateBetween(StatisticsType.SALES_YEARLY, startDate, endDate);
        }
        long months = ChronoUnit.MONTHS.between(startDate, endDate);
        if(months > 1) {
            return  statisticsRepository.findAllByTypeAndDateBetween(StatisticsType.SALES_MONTHLY, startDate, endDate);
        }
        return  statisticsRepository.findAllByTypeAndDateBetween(StatisticsType.SALES_DAILY, startDate, endDate);
    }

    public List<Statistics> getPVStatistics(LocalDate startDate, LocalDate endDate){
        long years = ChronoUnit.YEARS.between(startDate, endDate);
        if(years > 1) {
            return  statisticsRepository.findAllByTypeAndDateBetween(StatisticsType.PV_YEARLY, startDate, endDate);
        }
        long months = ChronoUnit.MONTHS.between(startDate, endDate);
        if(months > 1) {
            return  statisticsRepository.findAllByTypeAndDateBetween(StatisticsType.PV_MONTHLY, startDate, endDate);
        }
        return  statisticsRepository.findAllByTypeAndDateBetween(StatisticsType.PV_DAILY, startDate, endDate);
    }

    public List<Statistics> getPVTotalStatistics() {

        Statistics totalPv = statisticsRepository.findOneByType(StatisticsType.PV_TOTAL);
        Statistics pvDaily = statisticsRepository.findOneByTypeAndDate(StatisticsType.PV_DAILY, LocalDate.now());
        List<Statistics> statistics = new ArrayList<>();
        statistics.add(totalPv);
        statistics.add(pvDaily);
        return statistics;
    }

    public List<Statistics> getUVTotalStatistics() {
        Statistics totalUv = statisticsRepository.findOneByType(StatisticsType.UV_TOTAL);
        Statistics uvDaily = statisticsRepository.findOneByTypeAndDate(StatisticsType.UV_DAILY, LocalDate.now());
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


    public void createProductStatistics(Product product) {
        LocalDate today = LocalDate.now();
        Statistics statistics = new Statistics();
        statistics.setCount(0);
        statistics.setName(product.getName());
        statistics.setType(StatisticsType.PRODUCT_SALES_MONTHLY);
        statistics.setDate(today.withDayOfMonth(today.lengthOfMonth()));
        statisticsRepository.save(statistics);

        statistics = new Statistics();
        statistics.setCount(0);
        statistics.setName(product.getName());
        statistics.setType(StatisticsType.PRODUCT_SALES);
        statisticsRepository.save(statistics);

        statistics = new Statistics();
        statistics.setCount(0);
        statistics.setName(product.getName());
        statistics.setType(StatisticsType.PRODUCT_CONVERSION);
        statisticsRepository.save(statistics);

        statistics = new Statistics();
        statistics.setCount(0);
        statistics.setName(product.getName());
        statistics.setType(StatisticsType.UV_PRODUCT_TOTAL);
        statisticsRepository.save(statistics);

        ProductStatistics ps = new ProductStatistics();
        ps.setCount(0);
        ps.setName(product.getName());
        ps.setM2m(0);
        productStatisticsRepository.save(ps);

    }

    public void createChannelStatistics(Channel c) {
        Statistics channelStat = new Statistics();
        channelStat.setType(StatisticsType.CHANNEL_SALES);
        channelStat.setCount(0);
        channelStat.setName(c.getName());
        statisticsRepository.save(channelStat);

    }
}
