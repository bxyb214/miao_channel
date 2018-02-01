package com.songzi.channel.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pingplusplus.model.Order;
import com.songzi.channel.domain.JhiOrder;
import com.songzi.channel.service.JhiOrderService;
import com.songzi.channel.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing JhiOrder.
 */
@RestController
@RequestMapping("/api")
@Api(value = "订单管理", description = "订单管理")
public class JhiOrderResource {

    private final Logger log = LoggerFactory.getLogger(JhiOrderResource.class);


    private final JhiOrderService jhiOrderService;

    public JhiOrderResource(JhiOrderService jhiOrderService) {
        this.jhiOrderService = jhiOrderService;
    }


    /**
     * GET  /orders : get all the orders.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of jhi_orders in body
     */
    @GetMapping("/orders")
    @ApiOperation(value = "获取订单列表")
    @Timed
    public ResponseEntity<List<JhiOrder>> getAllJhiOrders(Pageable pageable, @RequestBody JhiOrder order, @RequestParam LocalDate startDate, LocalDate endDate) {
        log.debug("REST request to get a page of Jhi_orders");
        Page<JhiOrder> page = jhiOrderService.queryByExampleByDateBetween(Example.of(order), pageable, startDate, endDate);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/orders");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @ApiOperation(value = "订单列表Excel导出，暂未实现")
    @GetMapping("/orders/export")
    @Timed
    //TODO Excel导出
    public ResponseEntity<List<JhiOrder>> getAllJhiOrders(@RequestBody JhiOrder order, @RequestParam LocalDate startDate, LocalDate endDate) {
        log.debug("REST request to get excel of Jhi_orders");
        List<JhiOrder> orders = jhiOrderService.queryByExampleByDateBetween(Example.of(order), startDate, endDate);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(orders));
    }
}
