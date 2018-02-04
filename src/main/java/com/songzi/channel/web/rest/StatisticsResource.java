package com.songzi.channel.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.songzi.channel.domain.Product;
import com.songzi.channel.domain.ProductStatistics;


import com.songzi.channel.domain.Statistics;
import com.songzi.channel.domain.enumeration.StatisticsType;
import com.songzi.channel.repository.StatisticsRepository;
import com.songzi.channel.service.StatisticsService;

import com.songzi.channel.web.rest.util.PaginationUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.List;

/**
 * REST controller for managing ProductStatistics.
 */
@RestController
@RequestMapping("/api")
@Api(value = "已测试：总体分析", description = "总体分析")
public class StatisticsResource {

    private final Logger log = LoggerFactory.getLogger(StatisticsResource.class);

    private static final String ENTITY_NAME = "statistics";

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    private final StatisticsService statisticsService;
    private final StatisticsRepository statisticsRepository;

    public StatisticsResource(StatisticsService statisticsService, StatisticsRepository statisticsRepository) {
        this.statisticsService = statisticsService;
        this.statisticsRepository = statisticsRepository;
    }

    /**
     * GET  /visits/statistics/sales : 总销售额
     * @return the ResponseEntity with status 200 (OK) and with body the StatisticsSalesTotalVM, or with status 404 (Not Found)
     */
    @GetMapping("/statistics/sales-total")
    @Timed
    @ApiOperation(value = "已测试：总销售额")
    public List<Statistics> getSalesTotalStatistics() {
        log.debug("REST request to get Visit : {}");
        List<Statistics> statistics = statisticsRepository.findAllByType(StatisticsType.SALES_TOTAL);
        return statistics;

    }

    /**
     * GET  /visits/statistics/pv :访问量（PV）
     * @return the ResponseEntity with status 200 (OK) and with body the StatisticsSalesTotalVM, or with status 404 (Not Found)
     */
    @GetMapping("/statistics/pv-total")
    @Timed
    @ApiOperation(value = "已测试：访问量（PV）")
    public List<Statistics> getPVTotalStatistics() {
        log.debug("REST request to get Visit : {}");
        List<Statistics> statistics = statisticsRepository.findAllByType(StatisticsType.PV_TOTAL);
        return statistics;

    }

    @GetMapping("/statistics/uv-total")
    @Timed
    @ApiOperation(value = "已测试：独立访客（UV)")
    public List<Statistics> getUVTotalStatistics() {
        log.debug("REST request to get Visit : {}");
        List<Statistics> statistics = statisticsRepository.findAllByType(StatisticsType.UV_TOTAL);
        return statistics;

    }

    @GetMapping("/statistics/pay-total")
    @Timed
    @ApiOperation(value = "已测试：支付笔数")
    public List<Statistics> getPayTotalStatistics() {
        log.debug("REST request to get Visit : {}");
        List<Statistics> statistics = statisticsRepository.findAllByType(StatisticsType.PAY_TOTAL);
        return statistics;

    }

    @GetMapping("/statistics/sales-daily")
    @Timed
    @ApiOperation(value = "已测试：销售额日统计")
    public  List<Statistics> getSalesDailyStatistics(@ApiParam(value = "2018-01-01", required = true) @RequestParam String startDateStr,
                                                     @ApiParam(value = "2018-01-31", required = true) @RequestParam String endDateStr) {
        log.debug("REST request to get Visit : {}");

        LocalDate startDate = LocalDate.parse(startDateStr, formatter);
        LocalDate endDate = LocalDate.parse(endDateStr, formatter);

        List<Statistics> statistics = statisticsRepository.findAllByTypeAndDateBetween(StatisticsType.SALES_DAILY, startDate, endDate);
        return statistics;
    }



    @GetMapping("/statistics/pv-daily")
    @Timed
    @ApiOperation(value = "已测试：访问量日统计")
    public  List<Statistics> getPVDailyStatistics(@ApiParam(value = "2018-01-01", required = true) @RequestParam String startDateStr,
                                                  @ApiParam(value = "2018-01-31", required = true) @RequestParam String endDateStr) {
        log.debug("REST request to get Visit : {}");

        LocalDate startDate = LocalDate.parse(startDateStr, formatter);
        LocalDate endDate = LocalDate.parse(endDateStr, formatter);
        List<Statistics> statistics = statisticsRepository.findAllByTypeAndDateBetween(StatisticsType.PV_DAILY, startDate, endDate);
        return statistics;
    }

    @GetMapping("/statistics/sales-monthly")
    @Timed
    @ApiOperation(value = "已测试：销售额月统计")
    public  List<Statistics> getSalesMonthlyStatistics(@ApiParam(value = "2018-01-01", required = true) @RequestParam String startDateStr,
                                                       @ApiParam(value = "2018-01-31", required = true) @RequestParam String endDateStr) {
        log.debug("REST request to get Visit : {}");

        LocalDate startDate = LocalDate.parse(startDateStr, formatter);
        LocalDate endDate = LocalDate.parse(endDateStr, formatter);
        List<Statistics> statistics = statisticsRepository.findAllByTypeAndDateBetween(StatisticsType.SALES_MONTHLY, startDate, endDate);
        return statistics;
    }



    @GetMapping("/statistics/pv-monthly")
    @Timed
    @ApiOperation(value = "已测试：访问量月统计")
    public  List<Statistics> getPVMonthlyStatistics(@ApiParam(value = "2018-01-01", required = true) @RequestParam String startDateStr,
                                                    @ApiParam(value = "2018-01-31", required = true) @RequestParam String endDateStr) {
        log.debug("REST request to get Visit : {}");

        LocalDate startDate = LocalDate.parse(startDateStr, formatter);
        LocalDate endDate = LocalDate.parse(endDateStr, formatter);
        List<Statistics> statistics = statisticsRepository.findAllByTypeAndDateBetween(StatisticsType.PV_MONTHLY, startDate, endDate);
        return statistics;
    }

    @GetMapping("/statistics/channel-sales")
    @Timed
    @ApiOperation(value = "已测试：渠道销售额")
    public  List<Statistics> getChannelSalesStatistics() {
        log.debug("REST request to get Visit : {}");
        List<Statistics> statistics = statisticsRepository.findAllByType(StatisticsType.CHANNEL_SALES);
        return statistics;
    }


    @GetMapping("/statistics/product-sales-price")
    @Timed
    @ApiOperation(value = "已测试：产品销售额")
    public  List<Statistics> getProductSalesPriceStatistics() {
        log.debug("REST request to get Visit : {}");
        List<Statistics> statistics = statisticsService.getProductSalesPriceStatistics();
        return statistics;
    }

    @GetMapping("/statistics/product-sales-number")
    @Timed
    @ApiOperation(value = "已测试：热门测试排行")
    public  ResponseEntity<List<ProductStatistics>> getProductSalesNumberStatistics(Pageable pageable) {
        log.debug("REST request to get Visit : {}");
        Page<ProductStatistics> page = statisticsService.getProductSalesNumberStatistics(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/statistics/product-sales-number");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/statistics/product-conversion")
    @Timed
    @ApiOperation(value = "已测试：产品转化率")
    public  List<Statistics> getProductConversionStatistics() {
        log.debug("REST request to get Visit : {}");
        List<Statistics> statistics = statisticsRepository.findAllByType(StatisticsType.PRODUCT_CONVERSION);
        return statistics;
    }



}
