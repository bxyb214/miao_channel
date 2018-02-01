package com.songzi.channel.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.songzi.channel.domain.Visit;

import com.songzi.channel.repository.VisitRepository;
import com.songzi.channel.service.VisitService;
import com.songzi.channel.service.dto.ChannelSalesPriceDTO;
import com.songzi.channel.service.dto.PVDTO;
import com.songzi.channel.service.dto.SalesNumberDTO;
import com.songzi.channel.service.dto.SalesPriceDTO;
import com.songzi.channel.web.rest.errors.BadRequestAlertException;
import com.songzi.channel.web.rest.util.HeaderUtil;
import com.songzi.channel.web.rest.util.PaginationUtil;
import com.songzi.channel.web.rest.vm.*;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Visit.
 */
@RestController
@RequestMapping("/api")
@Api(value = "总体分析", description = "总体分析")
public class VisitResource {

    private final Logger log = LoggerFactory.getLogger(VisitResource.class);

    private static final String ENTITY_NAME = "visit";

    private final VisitRepository visitRepository;

    private final VisitService visitService;

    public VisitResource(VisitRepository visitRepository, VisitService visitService) {
        this.visitRepository = visitRepository;
        this.visitService = visitService;
    }


    /**
     * GET  /visits/statistics/sales-price : 总销售额
     * @return the ResponseEntity with status 200 (OK) and with body the VisitSalesPriceStatisticsVM, or with status 404 (Not Found)
     */
    @GetMapping("/visits/statistics/sales-price")
    @Timed
    @ApiOperation(value = "总销售额")
    public ResponseEntity<VisitSalesPriceStatisticsVM> getSalesPriceStatistics() {
        log.debug("REST request to get Visit : {}");
        VisitSalesPriceStatisticsVM visitSalesTotalVM = visitService.getSalesPriceStatistics();
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(visitSalesTotalVM));

    }

    /**
     * GET  /visits/statistics/pv :访问量（PV）
     * @return the ResponseEntity with status 200 (OK) and with body the VisitSalesPriceStatisticsVM, or with status 404 (Not Found)
     */
    @GetMapping("/visits/statistics/pv")
    @Timed
    @ApiOperation(value = "访问量（PV）")
    public ResponseEntity<VisitPVStatisticsVM> getPVStatistics() {
        log.debug("REST request to get Visit : {}");
        VisitPVStatisticsVM visitPVStatisticsVM = visitService.getPVStatistics();
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(visitPVStatisticsVM));

    }

    @GetMapping("/visits/statistics/uv")
    @Timed
    @ApiOperation(value = "独立访客（UV)")
    public ResponseEntity<VisitUVStatisticsVM> getUVStatistics() {
        log.debug("REST request to get Visit : {}");
        VisitUVStatisticsVM visitUVStatisticsVM = visitService.getUVStatistics();
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(visitUVStatisticsVM));

    }

    @GetMapping("/visits/statistics/sale-number")
    @Timed
    @ApiOperation(value = "支付笔数")
    public ResponseEntity<VisitSalesNumberStatisticsVM> getSalesNumberStatistics() {
        log.debug("REST request to get Visit : {}");
        VisitSalesNumberStatisticsVM visitSalesNumberStatisticsVM = visitService.getSalesNumberStatistics();
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(visitSalesNumberStatisticsVM));

    }

    @GetMapping("/visits/sale-prices")
    @Timed
    @ApiOperation(value = "销售额日统计")
    public ResponseEntity<List<SalesPriceDTO>> getSalesPrices(@RequestParam LocalDate startDate, LocalDate endDate) {
        log.debug("REST request to get Visit : {}");
        List<SalesPriceDTO> salesPriceDTOS = visitRepository.getSalesPricesByDateBetween(startDate, endDate);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(salesPriceDTOS));
    }



    @GetMapping("/visits/sale-numbers")
    @Timed
    @ApiOperation(value = "销售量日统计")
    public ResponseEntity<List<SalesNumberDTO>> getSalesNumbers(@RequestParam LocalDate startDate, LocalDate endDate) {
        log.debug("REST request to get Visit : {}");
        List<SalesNumberDTO> salesNumberDTOS = visitRepository.getSalesNumbersByDateBetween(startDate, endDate);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(salesNumberDTOS));
    }

    @GetMapping("/visits/pvs")
    @Timed
    @ApiOperation(value = "pv日统计")
    public ResponseEntity<List<PVDTO>> getPVs(@RequestParam LocalDate startDate, LocalDate endDate) {
        log.debug("REST request to get Visit : {}");
        List<PVDTO> pvdtos = visitRepository.getPVsByDateBetween(startDate, endDate);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pvdtos));
    }

    @GetMapping("/visits/channel-sale-prices")
    @Timed
    @ApiOperation(value = "渠道销售额排名")
    public ResponseEntity<List<ChannelSalesPriceDTO>> GetChannelsOrderBySalesPrice(@RequestParam LocalDate startDate, LocalDate endDate) {
        log.debug("REST request to get Visit : {}");
        List<ChannelSalesPriceDTO> channelSalesPriceDTOS = visitService.getChannelsOrderBySalesPrice(startDate, endDate);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(channelSalesPriceDTOS));
    }
}

