package com.songzi.channel.service;

import com.songzi.channel.config.Constants;
import com.songzi.channel.domain.*;
import com.songzi.channel.domain.enumeration.OrderStatus;
import com.songzi.channel.domain.enumeration.PersonaType;
import com.songzi.channel.domain.enumeration.StatisticsType;
import com.songzi.channel.domain.enumeration.Status;
import com.songzi.channel.repository.*;
import com.songzi.channel.service.manager.IPManager;
import com.songzi.channel.service.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;


/**
 *  实时  PV_TOTAL, PV_DAILY, UV_TOTAL, UV_DAILY,
 *  查询时 SALES_TOTAL_D2D, PAY_TOTAL, PAY_DAILY, SALES_TOTAL, SALES_DAILY
 *
 *  每天
 *  PAY_TOTAL,
 *  SALES_TOTAL,
 *  SALES_DAILY,
 *  SALES_PRODUCT_CHANNEL_DAILY,
 *  SALES_PRODUCT_CHANNEL_TOTAL,
 *  UV_PRODUCT_CHANNEL_DAILY,
 *  UV_PRODUCT_CHANNEL_TOTAL,
 *  PV_PRODUCT_CHANNEL_DAILY
 *  PV_PRODUCT_CHANNEL_TOTAL,
 *  PRODUCT_CONVERSION
 *
 *  每月
 *  SALES_MONTHLY,
 *  PV_MONTHLY,
 *  SALES_PRODUCT_CHANNEL_MONTHLY
 *
 *  每年
 *  SALES_YEARLY
 *  PV_YEARLY,
 */

@Service
@Transactional
public class ScheduleService {

    private final Logger log = LoggerFactory.getLogger(ScheduleService.class);

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    private final JhiOrderRepository orderRepository;

    private final StatisticsRepository statisticsRepository;

    private final ProductRepository productRepository;

    private final ChannelRepository channelRepository;

    private final VisitRepository visitRepository;

    private final ChannelStatisticsRepository channelStatisticsRepository;

    private final PersonaRepository personaRepository;

    private final IPManager ipManager;

    public ScheduleService(JhiOrderRepository orderRepository,
                           StatisticsRepository statisticsRepository,
                           ProductRepository productRepository,
                           ChannelRepository channelRepository,
                           VisitRepository visitRepository,
                           ChannelStatisticsRepository channelStatisticsRepository,
                           PersonaRepository personaRepository,
                           IPManager ipManager){
        this.orderRepository = orderRepository;
        this.statisticsRepository = statisticsRepository;
        this.productRepository = productRepository;
        this.channelRepository = channelRepository;
        this.visitRepository = visitRepository;
        this.channelStatisticsRepository = channelStatisticsRepository;
        this.personaRepository = personaRepository;
        this.ipManager = ipManager;
    }

    /**
     * 每天1点定时任务
     */
    @Scheduled(cron = "0 0 1 * * ?")
    void scheduledDailyTask(){
        log.info("ScheduleService daily task run ...");
        LocalDate today = LocalDate.now();
        dailyTask(today);
    }

    public void dailyTask(LocalDate today){

        LocalDate yesterday = today.minusDays(1);
        clearByUpdateDate(today);

        List<Channel> channels = channelRepository.findAllByStatus(Status.NORMAL);
        List<Product> products = productRepository.findAll();

        //PAY_TOTAL
        int payDaily = orderRepository.countByStatusAndOrderDateBetween(OrderStatus.已支付, DateUtil.getStartOfDay(yesterday), DateUtil.getEndOfDay(yesterday));
        double payTotalBeforeYesterday = 0.0;

        Object payTotalBeforeYesterdayObj = statisticsRepository.getCountByTypeAndDate(StatisticsType.PAY_TOTAL.toString(), yesterday.minusDays(1));
        if (payTotalBeforeYesterdayObj != null){
            payTotalBeforeYesterday = (double) payTotalBeforeYesterdayObj;
        }

        Statistics statistics = new Statistics();
        statistics.setChannelCode(null);
        statistics.setProductCode(null);
        statistics.setName(Constants.PAY_TOTAL);
        statistics.setType(StatisticsType.PAY_TOTAL);
        statistics.setCount(payTotalBeforeYesterday + payDaily);
        statistics.setDate(yesterday);
        statistics.setUpdateDate(today);
        statisticsRepository.save(statistics);

        //SALES_DAILY
        Object salesDailyObj = orderRepository.sumPriceByStatusAndOrderDateBetween("已支付", DateUtil.getStartOfDay(yesterday), DateUtil.getEndOfDay(yesterday));
        Double salesDaily = (Double) salesDailyObj;
        statistics = new Statistics();
        statistics.setChannelCode(null);
        statistics.setProductCode(null);
        statistics.setDate(yesterday);
        statistics.setCount(salesDaily);
        statistics.setType(StatisticsType.SALES_DAILY);
        statistics.setName(Constants.SALES_DAILY);
        statistics.setUpdateDate(today);
        statisticsRepository.save(statistics);

        //SALES_TOTAL
        double salesTotalBeforeYesterday = 0.0;
        Object salesTotalBeforeYesterdayObj = statisticsRepository.getCountByTypeAndDate(StatisticsType.SALES_TOTAL.toString(), yesterday.minusDays(1));
        if (salesTotalBeforeYesterdayObj != null){
            salesTotalBeforeYesterday = (double) salesTotalBeforeYesterdayObj;
        }
        statistics.setChannelCode(null);
        statistics.setProductCode(null);
        statistics.setDate(yesterday);
        statistics.setType(StatisticsType.SALES_DAILY);
        statistics.setName(Constants.SALES_DAILY);
        statistics.setUpdateDate(today);
        statistics.setCount(salesTotalBeforeYesterday + salesDaily);
        statisticsRepository.save(statistics);

        //SALES_PRODUCT_CHANNEL_DAILY
        for (Channel c : channels){

            salesDaily = orderRepository.sumPriceByChannelIdAndStatusAndOrderDateBetween(c.getId(), OrderStatus.已支付.toString(), DateUtil.getStartOfDay(yesterday), DateUtil.getEndOfDay(yesterday));
            statistics = new Statistics();
            statistics.setProductCode("0");
            statistics.setChannelCode(c.getCode());
            statistics.setDate(yesterday);
            statistics.setName(Constants.SALES_PRODUCT_CHANNEL_DAILY);
            statistics.setType(StatisticsType.SALES_PRODUCT_CHANNEL_DAILY);
            statistics.setCount(salesDaily);
            statistics.setUpdateDate(today);
            statisticsRepository.save(statistics);
        }

        for (Product p : products){
            salesDaily = orderRepository.sumPriceByProductIdAndStatusAndOrderDateBetween(p.getId(), OrderStatus.已支付.toString(), DateUtil.getStartOfDay(yesterday), DateUtil.getEndOfDay(yesterday));
            statistics = new Statistics();
            statistics.setProductCode(p.getCode());
            statistics.setChannelCode("0");
            statistics.setDate(yesterday);
            statistics.setName(Constants.SALES_PRODUCT_CHANNEL_DAILY);
            statistics.setType(StatisticsType.SALES_PRODUCT_CHANNEL_DAILY);
            statistics.setCount(salesDaily);
            statistics.setUpdateDate(today);
            statisticsRepository.save(statistics);
        }

        for (Channel c : channels){
            for (Product p : products){
                salesDaily = orderRepository.sumPriceByChannelIdAndProductIdAndStatusAndOrderDateBetween(c.getId(), p.getId(), OrderStatus.已支付.toString(), DateUtil.getStartOfDay(yesterday), DateUtil.getEndOfDay(yesterday));
                statistics = new Statistics();
                statistics.setProductCode(p.getCode());
                statistics.setChannelCode(c.getCode());
                statistics.setDate(yesterday);
                statistics.setName(Constants.SALES_PRODUCT_CHANNEL_DAILY);
                statistics.setType(StatisticsType.SALES_PRODUCT_CHANNEL_DAILY);
                statistics.setCount(salesDaily);
                statistics.setUpdateDate(today);
                statisticsRepository.save(statistics);
            }
        }

        //SALES_PRODUCT_CHANNEL_TOTAL
        for (Channel c : channels){
            statistics = statisticsRepository.findOneByTypeAndProductCodeAndChannelCode(StatisticsType.SALES_PRODUCT_CHANNEL_TOTAL,"0", c.getCode());
            Statistics statisticsDaily = statisticsRepository.findOneByTypeAndDateAndProductCodeAndChannelCode(StatisticsType.SALES_PRODUCT_CHANNEL_DAILY, yesterday,"0", c.getCode());

            double statisticsCount = 0.0;
            double statisticsDailyCount = 0.0;
            if (statistics != null){
                statisticsCount = statistics.getCount();
            } else {
                statistics = new Statistics();
                statistics.setType(StatisticsType.SALES_PRODUCT_CHANNEL_TOTAL);
                statistics.setName(c.getName());
                statistics.setChannelCode(c.getCode());
                statistics.setProductCode("0");
            }
            if (statisticsDaily != null){
                statisticsDailyCount = statisticsDaily.getCount();
            }
            statistics.setCount(statisticsCount + statisticsDailyCount);
            statistics.setDate(yesterday);
            statistics.setUpdateDate(today);
            statisticsRepository.save(statistics);
        }

        for (Product p : products){
            statistics = statisticsRepository.findOneByTypeAndProductCodeAndChannelCode(StatisticsType.SALES_PRODUCT_CHANNEL_TOTAL,p.getCode(), "0");
            Statistics statisticsDaily = statisticsRepository.findOneByTypeAndDateAndProductCodeAndChannelCode(StatisticsType.SALES_PRODUCT_CHANNEL_DAILY, yesterday, p.getCode(), "0");
            double statisticsCount = 0.0;
            double statisticsDailyCount = 0.0;
            if (statistics != null){
                statisticsCount = statistics.getCount();
            } else {
                statistics = new Statistics();
                statistics.setType(StatisticsType.SALES_PRODUCT_CHANNEL_TOTAL);
                statistics.setName(p.getName());
                statistics.setProductCode(p.getCode());
                statistics.setChannelCode("0");
            }
            if (statisticsDaily != null){
                statisticsDailyCount = statisticsDaily.getCount();
            }
            statistics.setCount(statisticsCount + statisticsDailyCount);
            statistics.setDate(yesterday);
            statistics.setUpdateDate(today);
            statisticsRepository.save(statistics);
        }

        //UV_PRODUCT_CHANNEL_DAILY
        for (Channel c : channels){
            Integer uvDaily = visitRepository.countDistinctByDateAndProductCodeAndChannelCode(yesterday, "0", c.getCode());
            statistics = new Statistics();
            statistics.setProductCode("0");
            statistics.setChannelCode(c.getCode());
            statistics.setDate(yesterday);
            statistics.setName(Constants.UV_DAILY);
            statistics.setType(StatisticsType.UV_PRODUCT_CHANNEL_DAILY);
            statistics.setCount((double)uvDaily);
            statistics.setUpdateDate(today);
            statisticsRepository.save(statistics);
        }

        for (Product p : products){
            Integer uvDaily = visitRepository.countDistinctByDateAndProductCodeAndChannelCode(yesterday, p.getCode(), "0");
            statistics = new Statistics();
            statistics.setProductCode(p.getCode());
            statistics.setChannelCode("0");
            statistics.setDate(yesterday);
            statistics.setName(Constants.UV_DAILY);
            statistics.setType(StatisticsType.UV_PRODUCT_CHANNEL_DAILY);
            statistics.setCount((double)uvDaily);
            statistics.setUpdateDate(today);
            statisticsRepository.save(statistics);
        }


        for (Channel c : channels){
            for (Product p : products){
                Integer uvDaily = visitRepository.countDistinctByDateAndProductCodeAndChannelCode(yesterday, p.getCode(), c.getCode());
                statistics = new Statistics();
                statistics.setProductCode(p.getCode());
                statistics.setChannelCode(c.getCode());
                statistics.setDate(yesterday);
                statistics.setName(Constants.UV_DAILY);
                statistics.setType(StatisticsType.UV_PRODUCT_CHANNEL_DAILY);
                statistics.setCount((double)uvDaily);
                statistics.setUpdateDate(today);
                statisticsRepository.save(statistics);
            }
        }

        //UV_PRODUCT_CHANNEL_TOTAL
        for (Channel c : channels){
            double uvDaily = 0.0;
            Object uvDailyObj = statisticsRepository.getCountByTypeAndDateAndProductCodeAndChannelCode(StatisticsType.UV_PRODUCT_CHANNEL_DAILY.toString(), yesterday, "0", c.getCode());
            if (uvDailyObj != null){
                uvDaily = (double)uvDailyObj;
            }

            statistics = statisticsRepository.findOneByTypeAndProductCodeAndChannelCode(StatisticsType.UV_PRODUCT_CHANNEL_TOTAL, "0", c.getCode());
            if (statistics == null){
                statistics = new Statistics();
                statistics.setProductCode("");
                statistics.setChannelCode(c.getCode());
                statistics.setDate(yesterday);
                statistics.setName(Constants.UV_TOTAL);
                statistics.setCount(0.0);
                statistics.setType(StatisticsType.UV_PRODUCT_CHANNEL_TOTAL);
            }
            statistics.setCount(statistics.getCount() + uvDaily);
            statistics.setUpdateDate(today);
            statisticsRepository.save(statistics);
        }

        for (Product p : products){
            double uvDaily = 0.0;
            Object uvDailyObj = statisticsRepository.getCountByTypeAndDateAndProductCodeAndChannelCode(StatisticsType.UV_PRODUCT_CHANNEL_DAILY.toString(), yesterday, p.getCode(), "0");
            if (uvDailyObj != null){
                uvDaily = (double)uvDailyObj;
            }
            statistics = statisticsRepository.findOneByTypeAndProductCodeAndChannelCode(StatisticsType.UV_PRODUCT_CHANNEL_TOTAL, p.getCode(), "0");
            if (statistics == null){
                statistics = new Statistics();
                statistics.setProductCode(p.getCode());
                statistics.setChannelCode("0");
                statistics.setDate(yesterday);
                statistics.setName(Constants.UV_TOTAL);
                statistics.setCount(0.0);
                statistics.setType(StatisticsType.UV_PRODUCT_CHANNEL_TOTAL);
            }
            statistics.setCount(statistics.getCount() + uvDaily);
            statistics.setUpdateDate(today);
            statisticsRepository.save(statistics);
        }


        for (Channel c : channels){
            for (Product p : products){
                double uvDaily = 0.0;
                Object uvDailyObj = statisticsRepository.getCountByTypeAndDateAndProductCodeAndChannelCode(StatisticsType.UV_PRODUCT_CHANNEL_DAILY.toString(), yesterday, p.getCode(), c.getCode());
                if (uvDailyObj != null){
                    uvDaily = (double)uvDailyObj;
                }
                statistics = statisticsRepository.findOneByTypeAndProductCodeAndChannelCode(StatisticsType.UV_PRODUCT_CHANNEL_TOTAL, p.getCode(), c.getCode());
                if (statistics == null){
                    statistics = new Statistics();
                    statistics.setProductCode(p.getCode());
                    statistics.setChannelCode(c.getCode());
                    statistics.setDate(yesterday);
                    statistics.setName(Constants.UV_TOTAL);
                    statistics.setCount(0.0);
                    statistics.setType(StatisticsType.UV_PRODUCT_CHANNEL_TOTAL);
                }
                statistics.setCount(statistics.getCount() + uvDaily);
                statistics.setUpdateDate(today);
                statisticsRepository.save(statistics);
            }
        }

        //PV_PRODUCT_CHANNEL_DAILY
        for (Channel c : channels){
            Integer pvDaily = visitRepository.countByDateAndProductCodeAndChannelCode(yesterday, "0", c.getCode());
            statistics = new Statistics();
            statistics.setProductCode("0");
            statistics.setChannelCode(c.getCode());
            statistics.setDate(yesterday);
            statistics.setName(Constants.PV_DAILY);
            statistics.setType(StatisticsType.PV_PRODUCT_CHANNEL_DAILY);
            statistics.setCount((double)pvDaily);
            statistics.setUpdateDate(today);
            statisticsRepository.save(statistics);
        }

        for (Product p : products){
            Integer pvDaily = visitRepository.countByDateAndProductCodeAndChannelCode(yesterday, p.getCode(), "0");
            statistics = new Statistics();
            statistics.setProductCode(p.getCode());
            statistics.setChannelCode("0");
            statistics.setDate(yesterday);
            statistics.setName(Constants.PV_DAILY);
            statistics.setType(StatisticsType.PV_PRODUCT_CHANNEL_DAILY);
            statistics.setCount((double)pvDaily);
            statistics.setUpdateDate(today);
            statisticsRepository.save(statistics);
        }


        for (Channel c : channels){
            for (Product p : products){
                Integer pvDaily = visitRepository.countByDateAndProductCodeAndChannelCode(yesterday, p.getCode(), c.getCode());
                statistics = new Statistics();
                statistics.setProductCode(p.getCode());
                statistics.setChannelCode(c.getCode());
                statistics.setDate(yesterday);
                statistics.setName(Constants.PV_DAILY);
                statistics.setType(StatisticsType.PV_PRODUCT_CHANNEL_DAILY);
                statistics.setCount((double)pvDaily);
                statistics.setUpdateDate(today);
                statisticsRepository.save(statistics);
            }
        }

        //PV_PRODUCT_CHANNEL_TOTAL
        for (Channel c : channels){
            double pvDaily = 0.0;
            Object pvDailyObj = statisticsRepository.getCountByTypeAndDateAndProductCodeAndChannelCode(StatisticsType.PV_PRODUCT_CHANNEL_DAILY.toString(), yesterday, "0", c.getCode());
            if (pvDailyObj != null){
                pvDaily = (double)pvDailyObj;
            }
            statistics = statisticsRepository.findOneByTypeAndProductCodeAndChannelCode(StatisticsType.PV_PRODUCT_CHANNEL_TOTAL, "0", c.getCode());
            if (statistics == null){
                statistics = new Statistics();
                statistics.setProductCode("");
                statistics.setChannelCode(c.getCode());
                statistics.setDate(yesterday);
                statistics.setName(Constants.PV_TOTAL);
                statistics.setCount(0.0);
                statistics.setType(StatisticsType.PV_PRODUCT_CHANNEL_TOTAL);
            }
            statistics.setCount(statistics.getCount() + pvDaily);
            statistics.setUpdateDate(today);
            statisticsRepository.save(statistics);
        }

        for (Product p : products){
            double pvDaily = 0.0;
            Object pvDailyObj = statisticsRepository.getCountByTypeAndDateAndProductCodeAndChannelCode(StatisticsType.PV_PRODUCT_CHANNEL_DAILY.toString(), yesterday, p.getCode(), "0");
            if (pvDailyObj != null){
                pvDaily = (double)pvDailyObj;
            }
            statistics = statisticsRepository.findOneByTypeAndProductCodeAndChannelCode(StatisticsType.PV_PRODUCT_CHANNEL_TOTAL, p.getCode(), "0");
            if (statistics == null){
                statistics = new Statistics();
                statistics.setProductCode(p.getCode());
                statistics.setChannelCode("0");
                statistics.setDate(yesterday);
                statistics.setName(Constants.PV_TOTAL);
                statistics.setCount(0.0);
                statistics.setType(StatisticsType.PV_PRODUCT_CHANNEL_TOTAL);
            }
            statistics.setCount(statistics.getCount() + pvDaily);
            statistics.setUpdateDate(today);
            statisticsRepository.save(statistics);
        }


        for (Channel c : channels){
            for (Product p : products){
                double pvDaily = 0.0;
                Object pvDailyObj = statisticsRepository.getCountByTypeAndDateAndProductCodeAndChannelCode(StatisticsType.PV_PRODUCT_CHANNEL_DAILY.toString(), yesterday, p.getCode(), c.getCode());
                if (pvDailyObj != null){
                    pvDaily = (double) pvDailyObj;
                }
                statistics = statisticsRepository.findOneByTypeAndProductCodeAndChannelCode(StatisticsType.PV_PRODUCT_CHANNEL_TOTAL, p.getCode(), c.getCode());
                if (statistics == null){
                    statistics = new Statistics();
                    statistics.setProductCode(p.getCode());
                    statistics.setChannelCode(c.getCode());
                    statistics.setDate(yesterday);
                    statistics.setName(Constants.PV_TOTAL);
                    statistics.setCount(0.0);
                    statistics.setType(StatisticsType.PV_PRODUCT_CHANNEL_TOTAL);
                }
                statistics.setCount(statistics.getCount() + pvDaily);
                statistics.setUpdateDate(today);
                statisticsRepository.save(statistics);
            }
        }


        //PRODUCT_CONVERSION
        statisticsRepository.deleteAllByType(StatisticsType.PRODUCT_CONVERSION);
        for (Product p : products){
            Statistics saleStatistics = statisticsRepository.findOneByTypeAndProductCodeAndChannelCode(StatisticsType.SALES_PRODUCT_CHANNEL_TOTAL, p.getCode(), "0");
            Statistics uvStatistics = statisticsRepository.findOneByTypeAndProductCodeAndChannelCode(StatisticsType.UV_PRODUCT_CHANNEL_TOTAL, p.getCode(), "0");
            Double conversion = 0.0;
            if (uvStatistics.getCount() != 0) {
                conversion = saleStatistics.getCount() / uvStatistics.getCount();
            }
            statistics = new Statistics();
            statistics.setType(StatisticsType.PRODUCT_CONVERSION);
            statistics.setCount(conversion);
            statistics.setName(Constants.PRODUCT_CONVERSION);
            statistics.setDate(null);
            statistics.setChannelCode("0");
            statistics.setProductCode(p.getCode());
            statistics.setDate(yesterday);
            statistics.setUpdateDate(today);
            statisticsRepository.save(statistics);
        }



        //月任务
        monthlyTask(today, products, channels);

        //渠道统计
        channelStatisticsDaily(today, products, channels);

        //任务画像统计
        personaStatisticsDaily(today);
    }

    /**
     * 支持重跑
     * @param today
     */
    private void clearByUpdateDate(LocalDate today) {
        List<Statistics> statistics = statisticsRepository.findAllByUpdateDate(today);
        for (Statistics s : statistics){
            if (!(s.getType().equals(StatisticsType.UV_TOTAL) || s.getType().equals(StatisticsType.UV_DAILY) || s.getType().equals(StatisticsType.PV_TOTAL) || s.getType().equals(StatisticsType.PV_DAILY))){
                statisticsRepository.delete(s.getId());
            }
        }
        //TODO personaRepository 需要支持重跑
        personaRepository.deleteAll();

        channelStatisticsRepository.deleteAllByUpdateDate(today);
    }


    private void monthlyTask(LocalDate today, List<Product> products, List<Channel> channels){

        LocalDate lastDayOfMonth = today.minusDays(1);

        if (lastDayOfMonth.equals(lastDayOfMonth.withDayOfMonth(lastDayOfMonth.lengthOfMonth()))){
            log.info("ScheduleService monthly task run ...");
            LocalDate firstDayOfMonth = lastDayOfMonth.withDayOfMonth(1);

            //SALES_MONTHLY
            Double salesMonthly = statisticsRepository.getSumByTypeAndDateBetween(StatisticsType.SALES_DAILY, firstDayOfMonth, lastDayOfMonth);
            Statistics statistics = statisticsRepository.findOneByTypeAndDate(StatisticsType.SALES_MONTHLY, lastDayOfMonth);
            if (statistics == null){
                statistics = new Statistics();
                statistics.setProductCode("0");
                statistics.setChannelCode("0");
                statistics.setDate(lastDayOfMonth);
                statistics.setName(Constants.SALES_MONTHLY);
                statistics.setType(StatisticsType.SALES_MONTHLY);
            }
            statistics.setCount(salesMonthly);
            statistics.setUpdateDate(today);
            statisticsRepository.save(statistics);

            //PV_MONTHLY
            Double pvMonthly = statisticsRepository.getSumByTypeAndDateBetween(StatisticsType.PV_MONTHLY, firstDayOfMonth, lastDayOfMonth);
            statistics = statisticsRepository.findOneByTypeAndDate(StatisticsType.PV_MONTHLY, lastDayOfMonth);
            if (statistics == null){
                statistics = new Statistics();
                statistics.setProductCode("0");
                statistics.setChannelCode("0");
                statistics.setDate(lastDayOfMonth);
                statistics.setName(Constants.PV_MONTHLY);
                statistics.setType(StatisticsType.PV_MONTHLY);
            }
            statistics.setCount(pvMonthly);
            statistics.setUpdateDate(today);
            statisticsRepository.save(statistics);

            //SALES_PRODUCT_CHANNEL_MONTHLY
            for (Channel c : channels){
                salesMonthly = statisticsRepository.getSumByTypeAndDateBetweenAndProductCodeAndChannelCode(StatisticsType.SALES_PRODUCT_CHANNEL_DAILY, firstDayOfMonth, lastDayOfMonth, "0", c.getCode());
                statistics = new Statistics();
                statistics.setProductCode("0");
                statistics.setChannelCode(c.getCode());
                statistics.setDate(lastDayOfMonth);
                statistics.setName(Constants.SALES_TOTAL);
                statistics.setType(StatisticsType.SALES_PRODUCT_CHANNEL_MONTHLY);
                statistics.setCount(salesMonthly);
                statistics.setUpdateDate(today);
                statisticsRepository.save(statistics);
            }

            for (Product p : products){
                salesMonthly = statisticsRepository.getSumByTypeAndDateBetweenAndProductCodeAndChannelCode(StatisticsType.SALES_PRODUCT_CHANNEL_DAILY, firstDayOfMonth, lastDayOfMonth, p.getCode(), "0");
                statistics = new Statistics();
                statistics.setProductCode(p.getCode());
                statistics.setChannelCode("0");
                statistics.setDate(lastDayOfMonth);
                statistics.setName(Constants.SALES_TOTAL);
                statistics.setType(StatisticsType.SALES_PRODUCT_CHANNEL_MONTHLY);
                statistics.setCount(salesMonthly);
                statistics.setUpdateDate(today);
                statisticsRepository.save(statistics);
            }


            for (Channel c : channels){
                for (Product p : products){
                    salesMonthly = statisticsRepository.getSumByTypeAndDateBetweenAndProductCodeAndChannelCode(StatisticsType.SALES_PRODUCT_CHANNEL_DAILY, firstDayOfMonth, lastDayOfMonth, p.getCode(), c.getCode());
                    statistics = new Statistics();
                    statistics.setProductCode(p.getCode());
                    statistics.setChannelCode(c.getCode());
                    statistics.setDate(lastDayOfMonth);
                    statistics.setName(Constants.SALES_TOTAL);
                    statistics.setType(StatisticsType.SALES_PRODUCT_CHANNEL_MONTHLY);
                    statistics.setCount(salesMonthly);
                    statistics.setUpdateDate(today);
                    statisticsRepository.save(statistics);
                }
            }

        }

        //年任务
        yearlyTask(today);
    }

    private void yearlyTask(LocalDate today){

        LocalDate lastDayOfYear = today.minusDays(1);

        if (lastDayOfYear.equals(lastDayOfYear.withDayOfYear(lastDayOfYear.lengthOfYear()))) {

            log.info("ScheduleService yearly task run ...");
            LocalDate firstDayOfYear = lastDayOfYear.withDayOfYear(1);

            //SALES_YEARLY
            //PV_YEARLY,
            Double salesyearly = statisticsRepository.getSumByTypeAndDateBetween(StatisticsType.SALES_DAILY, firstDayOfYear, lastDayOfYear);
            Statistics statistics = statisticsRepository.findOneByTypeAndDate(StatisticsType.SALES_MONTHLY, lastDayOfYear);
            if (statistics == null) {
                statistics = new Statistics();
                statistics.setProductCode("0");
                statistics.setChannelCode("0");
                statistics.setDate(lastDayOfYear);
                statistics.setName(Constants.SALES_YEARLY);
                statistics.setCount(0.0);
                statistics.setType(StatisticsType.SALES_YEARLY);
            }
            statistics.setCount(salesyearly);
            statistics.setUpdateDate(today);
            statisticsRepository.save(statistics);

            //PV_MONTHLY
            Double pvYearly = statisticsRepository.getSumByTypeAndDateBetween(StatisticsType.PV_MONTHLY, firstDayOfYear, lastDayOfYear);
            statistics = statisticsRepository.findOneByTypeAndDate(StatisticsType.PV_MONTHLY, lastDayOfYear);
            if (statistics == null) {
                statistics = new Statistics();
                statistics.setProductCode("0");
                statistics.setChannelCode("0");
                statistics.setDate(lastDayOfYear);
                statistics.setName(Constants.PV_YEARLY);
                statistics.setCount(0.0);
                statistics.setType(StatisticsType.PV_YEARLY);
            }
            statistics.setCount(pvYearly);
            statistics.setUpdateDate(today);
            statisticsRepository.save(statistics);
        }

    }

    private void channelStatisticsDaily(LocalDate today, List<Product> products, List<Channel> channels) {
        log.info("channelStatisticsDaily run");
        LocalDate yesterday = today.minusDays(1);

        for (Channel c : channels){

            for (Product p : products){

                ChannelStatistics channelStatistics = new ChannelStatistics();

                double pv = 0.0;
                Object pvObj = statisticsRepository.getCountByTypeAndDateAndProductCodeAndChannelCode(StatisticsType.PV_PRODUCT_CHANNEL_DAILY.toString(), yesterday,  p.getCode(),  c.getCode());
                if (pvObj != null){
                    pv = (double) pvObj;
                }
                channelStatistics.setPv(pv);

                double uv = 0.0;
                Object uvObj = statisticsRepository.getCountByTypeAndDateAndProductCodeAndChannelCode(StatisticsType.UV_PRODUCT_CHANNEL_DAILY.toString(), yesterday,p.getCode(), c.getCode());
                if (uvObj != null){
                    uv = (double) uvObj;
                }

                channelStatistics.setUv(uv);

                double orderNumber = orderRepository.countByChannelIdAndOrderDateBetween(c.getId(), DateUtil.getStartOfDay(yesterday), DateUtil.getEndOfDay(yesterday));
                channelStatistics.setOrderNumber(orderNumber);


                double orderRate = 0.0;
                if (uv != 0) {
                    orderRate = orderNumber / uv;
                }

                channelStatistics.setOrderRate(orderRate);

                double payNumber = orderRepository.countByChannelIdAndStatusAndOrderDateBetween(c.getId(), OrderStatus.已支付, DateUtil.getStartOfDay(yesterday), DateUtil.getEndOfDay(yesterday));
                channelStatistics.setPayNumber(payNumber);

                double payConversion = 0.0;
                if (uv != 0) {
                    payConversion = payNumber / uv;
                }

                channelStatistics.setPayConversion(payConversion);

                double salePrice = orderRepository.sumPriceByChannelIdAndStatusAndOrderDateBetween(c.getId(), OrderStatus.已支付.toString(), DateUtil.getStartOfDay(yesterday), DateUtil.getEndOfDay(yesterday));
                channelStatistics.setSalePrice(salePrice);

                double proportionPrice = orderRepository.sumProportionByPriceByChannelIdAndStatusAndOrderDateBetween(c.getId(), OrderStatus.已支付.toString(), DateUtil.getStartOfDay(yesterday), DateUtil.getEndOfDay(yesterday));
                channelStatistics.setProportionPrice(proportionPrice);


                double uvOutput = 0.0;
                if (uv != 0) {
                    uvOutput = salePrice / uv;
                }
                channelStatistics.setUvOutput(uvOutput);
                channelStatistics.setProductName(p.getName());
                channelStatistics.setChannelName(c.getName());
                channelStatistics.setChannelId(c.getId());
                channelStatistics.setUpdateDate(today);
                channelStatistics.setDate(yesterday);

                channelStatisticsRepository.save(channelStatistics);
            }
        }
    }

    private void personaStatisticsDaily(LocalDate today) {

        LocalDate yesterday = today.minusDays(1);

        List<JhiOrder> orders = orderRepository.findAllByStatus(OrderStatus.已支付);

        for (JhiOrder order : orders) {

            String city = ipManager.getCity(order.getIp());
            //city_sales_number
            Persona citySalesNumber = personaRepository.findOneByNameAndTypeAndChannelIdAndProductId(city, PersonaType.CITY_SALES_NUMBER, order.getChannelId(), order.getProductId());
            if (citySalesNumber == null) {
                citySalesNumber = new Persona();
                citySalesNumber.setName(city);
                citySalesNumber.setChannelId(order.getChannelId());
                citySalesNumber.setProductId(order.getProductId());
                citySalesNumber.setPersonaType(PersonaType.CITY_SALES_NUMBER);
                citySalesNumber.setCount(1);
            } else {
                citySalesNumber.setCount(citySalesNumber.getCount() + 1);
            }
            citySalesNumber.setUpdateDate(today);
            personaRepository.save(citySalesNumber);

            Persona citySalesNumberAll = personaRepository.findOneByNameAndTypeAndChannelIdAndProductId(city, PersonaType.CITY_SALES_NUMBER, 0L, 0L);
            if (citySalesNumberAll == null) {
                citySalesNumberAll = new Persona();
                citySalesNumberAll.setName(city);
                citySalesNumberAll.setChannelId(0L);
                citySalesNumberAll.setProductId(0L);
                citySalesNumberAll.setPersonaType(PersonaType.CITY_SALES_NUMBER);
                citySalesNumberAll.setCount(1);
            } else {
                citySalesNumberAll.setCount(citySalesNumberAll.getCount() + 1);
            }
            citySalesNumberAll.setUpdateDate(today);
            personaRepository.save(citySalesNumberAll);

            Persona citySalesNumberProduct = personaRepository.findOneByNameAndTypeAndChannelIdAndProductId(city, PersonaType.CITY_SALES_NUMBER, 0L, order.getProductId());
            if (citySalesNumberProduct == null) {
                citySalesNumberProduct = new Persona();
                citySalesNumberProduct.setName(city);
                citySalesNumberProduct.setChannelId(0L);
                citySalesNumberProduct.setProductId(order.getProductId());
                citySalesNumberProduct.setPersonaType(PersonaType.CITY_SALES_NUMBER);
                citySalesNumberProduct.setCount(1);
            } else {
                citySalesNumberProduct.setCount(citySalesNumberProduct.getCount() + 1);
            }
            citySalesNumberProduct.setUpdateDate(today);
            personaRepository.save(citySalesNumberProduct);

            Persona citySalesNumberChannel = personaRepository.findOneByNameAndTypeAndChannelIdAndProductId(city, PersonaType.CITY_SALES_NUMBER, order.getChannelId(), 0L);
            if (citySalesNumberChannel == null) {
                citySalesNumberChannel = new Persona();
                citySalesNumberChannel.setName(city);
                citySalesNumberChannel.setChannelId(order.getChannelId());
                citySalesNumberChannel.setProductId(0L);
                citySalesNumberChannel.setPersonaType(PersonaType.CITY_SALES_NUMBER);
                citySalesNumberChannel.setCount(1);
            } else {
                citySalesNumberChannel.setCount(citySalesNumberChannel.getCount() + 1);
            }
            citySalesNumberChannel.setUpdateDate(today);
            personaRepository.save(citySalesNumberChannel);


            //city_sales_price
            Persona citySalesPrice = personaRepository.findOneByNameAndTypeAndChannelIdAndProductId(city, PersonaType.CITY_SALES_PRICE, order.getChannelId(), order.getProductId());
            if (citySalesPrice == null) {
                citySalesPrice = new Persona();
                citySalesPrice.setName(city);
                citySalesPrice.setChannelId(order.getChannelId());
                citySalesPrice.setProductId(order.getProductId());
                citySalesPrice.setPersonaType(PersonaType.CITY_SALES_PRICE);
                citySalesPrice.setCount((int) Math.ceil(order.getPrice()));
            } else {
                citySalesPrice.setCount((int) Math.ceil((citySalesPrice.getCount() + order.getPrice())));
            }
            citySalesPrice.setUpdateDate(today);
            personaRepository.save(citySalesPrice);

            Persona citySalesPriceAll = personaRepository.findOneByNameAndTypeAndChannelIdAndProductId(city, PersonaType.CITY_SALES_PRICE, 0L, 0L);
            if (citySalesPriceAll == null) {
                citySalesPriceAll = new Persona();
                citySalesPriceAll.setName(city);
                citySalesPriceAll.setChannelId(0L);
                citySalesPriceAll.setProductId(0L);
                citySalesPriceAll.setPersonaType(PersonaType.CITY_SALES_PRICE);
                citySalesPriceAll.setCount((int) Math.ceil(order.getPrice()));
            } else {
                citySalesPriceAll.setCount((int) Math.ceil((citySalesPriceAll.getCount() + order.getPrice())));
            }
            citySalesPriceAll.setUpdateDate(today);
            personaRepository.save(citySalesPriceAll);

            Persona citySalesPriceProduct = personaRepository.findOneByNameAndTypeAndChannelIdAndProductId(city, PersonaType.CITY_SALES_PRICE, 0L, order.getProductId());
            if (citySalesPriceProduct == null) {
                citySalesPriceProduct = new Persona();
                citySalesPriceProduct.setName(city);
                citySalesPriceProduct.setChannelId(0L);
                citySalesPriceProduct.setProductId(order.getProductId());
                citySalesPriceProduct.setPersonaType(PersonaType.CITY_SALES_PRICE);
                citySalesPriceProduct.setCount((int) Math.ceil(order.getPrice()));
            } else {
                citySalesPriceProduct.setCount((int) Math.ceil((citySalesPriceProduct.getCount() + order.getPrice())));
            }
            citySalesPriceProduct.setUpdateDate(today);
            personaRepository.save(citySalesPriceProduct);

            Persona citySalesPriceChannel = personaRepository.findOneByNameAndTypeAndChannelIdAndProductId(city, PersonaType.CITY_SALES_PRICE, order.getChannelId(), 0L);
            if (citySalesPriceChannel == null) {
                citySalesPriceChannel = new Persona();
                citySalesPriceChannel.setName(city);
                citySalesPriceChannel.setChannelId(order.getChannelId());
                citySalesPriceChannel.setProductId(0L);
                citySalesPriceChannel.setPersonaType(PersonaType.CITY_SALES_PRICE);
                citySalesPriceChannel.setCount((int) Math.ceil(order.getPrice()));
            } else {
                citySalesPriceChannel.setCount((int) Math.ceil((citySalesPriceChannel.getCount() + order.getPrice())));
            }
            citySalesPriceChannel.setUpdateDate(today);
            personaRepository.save(citySalesPriceChannel);

            //sex
            String sexStr = order.getSexInfo().toString();
            Persona sex = personaRepository.findOneByNameAndTypeAndChannelIdAndProductId(sexStr, PersonaType.SEX, order.getChannelId(), order.getProductId());
            if (sex == null) {
                sex = new Persona();
                sex.setName(sexStr);
                sex.setChannelId(order.getChannelId());
                sex.setProductId(order.getProductId());
                sex.setPersonaType(PersonaType.SEX);
                sex.setCount(1);
            } else {
                sex.setCount(sex.getCount() + 1);
            }
            sex.setUpdateDate(today);
            personaRepository.save(sex);

            Persona sexAll = personaRepository.findOneByNameAndTypeAndChannelIdAndProductId(sexStr, PersonaType.SEX, 0L, 0L);
            if (sexAll == null) {
                sexAll = new Persona();
                sexAll.setName(sexStr);
                sexAll.setChannelId(0L);
                sexAll.setProductId(0L);
                sexAll.setPersonaType(PersonaType.SEX);
                sexAll.setCount(1);
            } else {
                sexAll.setCount(sexAll.getCount() + 1);
            }
            sexAll.setUpdateDate(today);
            personaRepository.save(sexAll);

            Persona sexProduct = personaRepository.findOneByNameAndTypeAndChannelIdAndProductId(sexStr, PersonaType.SEX, 0L, order.getProductId());
            if (sexProduct == null) {
                sexProduct = new Persona();
                sexProduct.setName(sexStr);
                sexProduct.setChannelId(0L);
                sexProduct.setProductId(order.getProductId());
                sexProduct.setPersonaType(PersonaType.SEX);
                sexProduct.setCount(1);
            } else {
                sexProduct.setCount(sexProduct.getCount() + 1);
            }
            sexProduct.setUpdateDate(today);
            personaRepository.save(sexProduct);


            Persona sexChannel = personaRepository.findOneByNameAndTypeAndChannelIdAndProductId(sexStr, PersonaType.SEX, order.getChannelId(), 0L);
            if (sexChannel == null) {
                sexChannel = new Persona();
                sexChannel.setName(sexStr);
                sexChannel.setChannelId(order.getChannelId());
                sexChannel.setProductId(0L);
                sexChannel.setPersonaType(PersonaType.SEX);
                sexChannel.setCount(1);
            } else {
                sexChannel.setCount(sexChannel.getCount() + 1);
            }
            sexChannel.setUpdateDate(today);
            personaRepository.save(sexChannel);
            //age

            LocalDate birth = LocalDate.parse(order.getBirthInfo(), formatter);
            int ageStr = Period.between(yesterday, birth).getYears();
            Persona age = personaRepository.findOneByNameAndTypeAndChannelIdAndProductId(getAgeRange(ageStr), PersonaType.AGE, order.getChannelId(), order.getProductId());
            if (age == null) {
                age = new Persona();
                age.setName(getAgeRange(ageStr));
                age.setChannelId(order.getChannelId());
                age.setProductId(order.getProductId());
                age.setPersonaType(PersonaType.AGE);
                age.setCount(1);
            } else {
                age.setCount(age.getCount() + 1);
            }
            age.setUpdateDate(today);
            personaRepository.save(age);

            Persona ageAll = personaRepository.findOneByNameAndTypeAndChannelIdAndProductId(getAgeRange(ageStr), PersonaType.AGE, 0L, 0L);
            if (ageAll == null) {
                ageAll = new Persona();
                ageAll.setName(getAgeRange(ageStr));
                ageAll.setChannelId(0L);
                ageAll.setProductId(0L);
                ageAll.setPersonaType(PersonaType.AGE);
                ageAll.setCount(1);
            } else {
                ageAll.setCount(ageAll.getCount() + 1);
            }
            ageAll.setUpdateDate(today);
            personaRepository.save(ageAll);

            Persona ageProduct = personaRepository.findOneByNameAndTypeAndChannelIdAndProductId(getAgeRange(ageStr), PersonaType.AGE, 0L, order.getProductId());
            if (ageProduct == null) {
                ageProduct = new Persona();
                ageProduct.setName(getAgeRange(ageStr));
                ageProduct.setChannelId(0L);
                ageProduct.setProductId(order.getProductId());
                ageProduct.setPersonaType(PersonaType.AGE);
                ageProduct.setCount(1);
            } else {
                ageProduct.setCount(ageProduct.getCount() + 1);
            }
            ageProduct.setUpdateDate(today);
            personaRepository.save(ageProduct);

            Persona ageChannel = personaRepository.findOneByNameAndTypeAndChannelIdAndProductId(getAgeRange(ageStr), PersonaType.AGE, order.getChannelId(), 0L);
            if (ageChannel == null) {
                ageChannel = new Persona();
                ageChannel.setName(getAgeRange(ageStr));
                ageChannel.setChannelId(order.getChannelId());
                ageChannel.setProductId(0L);
                ageChannel.setPersonaType(PersonaType.AGE);
                ageChannel.setCount(1);
            } else {
                ageChannel.setCount(ageChannel.getCount() + 1);
            }
            ageChannel.setUpdateDate(today);
            personaRepository.save(ageChannel);

        }

    }

    private String getAgeRange(int age){
        if (age < 17 && age > 0){
            return Constants.AGE_SEVENTEEN;
        }else if (age <= 24){
            return Constants.AGE_TWENTY_FOUR;
        }else if (age <= 29) {
            return Constants.AGE_TWENTY_NINE;
        }else if (age <= 34) {
            return Constants.AGE_THIRTY_FOUR;
        }else if (age <= 39){
            return Constants.AGE_THIRTY_NINE;
        }else if (age <= 50){
            return Constants.AGE_FIFTY;
        }else {
            return Constants.UNKNOWN;
        }
    }
}
