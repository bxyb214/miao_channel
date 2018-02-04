package com.songzi.channel.service;


import com.songzi.channel.domain.ProductStatistics;
import com.songzi.channel.domain.Statistics;
import com.songzi.channel.domain.enumeration.StatisticsType;
import com.songzi.channel.repository.ProductStatisticsRepository;
import com.songzi.channel.repository.StatisticsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            statistics.setCount(count + "");
            result.add(statistics);
        }
        return result;
    }

    public Page<ProductStatistics> getProductSalesNumberStatistics(Pageable pageable) {
        Page<ProductStatistics> page =  productStatisticsRepository.findAll(pageable);
        return page;

    }
}
