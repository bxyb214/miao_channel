package com.songzi.channel.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pingplusplus.model.Customs;
import com.pingplusplus.model.Event;
import com.pingplusplus.model.Order;
import com.pingplusplus.model.Webhooks;
import com.songzi.channel.domain.JhiOrder;
import com.songzi.channel.domain.enumeration.OrderStatus;
import com.songzi.channel.domain.enumeration.PayType;
import com.songzi.channel.repository.support.Range;
import com.songzi.channel.service.JhiOrderService;
import com.songzi.channel.service.util.DateUtil;
import com.songzi.channel.service.util.ExcelUtil;
import com.songzi.channel.web.rest.util.PaginationUtil;
import com.songzi.channel.web.rest.vm.OrderVM;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.undertow.security.idm.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing JhiOrder.
 */
@RestController
@RequestMapping("/api")
@Api(value = "订单管理", description = "已测：订单管理")
public class JhiOrderResource {

    private final Logger log = LoggerFactory.getLogger(JhiOrderResource.class);

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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
    @ApiOperation(value = "已测试：获取订单列表")
    @Timed
    public ResponseEntity<List<JhiOrder>> getAllJhiOrders(Pageable pageable,
                                                          @ApiParam(value = "开始时间, 2018-01-01") @RequestParam String startDateStr,
                                                          @ApiParam(value = "结束时间, 2018-01-31") @RequestParam String endDateStr,
                                                          @ApiParam(value = "订单号, 1") @RequestParam(required = false) String code,
                                                          @ApiParam(value = "订单状态") @RequestParam(required = false) OrderStatus status,
                                                          @ApiParam(value = "支付方式") @RequestParam(required = false) PayType payType,
                                                          @ApiParam(value = "渠道Id, 1") @RequestParam(required = false) Long channelId,
                                                          @ApiParam(value = "产品Id, 1") @RequestParam(required = false) Long productId) {
        log.debug("REST request to get a page of Jhi_orders");

        LocalDate startDate = LocalDate.parse(startDateStr, formatter);
        LocalDate endDate = LocalDate.parse(endDateStr, formatter);

        JhiOrder order = new JhiOrder();
        if(code != null)
            order.setCode(code);
        if(status != null)
            order.setStatus(status);
        if(payType != null)
            order.setPayType(payType);
        if(channelId != null)
            order.setChannelId(channelId);
        if(productId != null)
            order.setProductId(productId);

        List<Range<JhiOrder>> ranges = new ArrayList();

        Range<JhiOrder> orderDateRange = new Range<>("orderDate", DateUtil.getStartOfDay(startDate), DateUtil.getEndOfDay(endDate));
        ranges.add(orderDateRange);

        Page<JhiOrder> page = jhiOrderService.findAllWithRange(order, ranges, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/orders");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @ApiOperation(value = "已测：订单列表Excel导出，乱码")
    @GetMapping("/orders/export")
    @Timed
    public void exportAllJhiOrders(HttpServletRequest request, HttpServletResponse response,
                                   @ApiParam(value = "开始时间, 2018-01-01") @RequestParam String startDateStr,
                                   @ApiParam(value = "结束时间, 2018-01-31") @RequestParam String endDateStr,
                                   @ApiParam(value = "订单号, 1") @RequestParam(required = false) String code,
                                   @ApiParam(value = "订单状态") @RequestParam(required = false) OrderStatus status,
                                   @ApiParam(value = "支付方式") @RequestParam(required = false) PayType payType,
                                   @ApiParam(value = "渠道Id, 1") @RequestParam(required = false) Long channelId,
                                   @ApiParam(value = "产品Id, 1") @RequestParam(required = false) Long productId) throws Exception {
        log.debug("REST request to get excel of Jhi_orders");

        LocalDate startDate = LocalDate.parse(startDateStr, formatter);
        LocalDate endDate = LocalDate.parse(endDateStr, formatter);

        JhiOrder order = new JhiOrder();
        if(code != null)
            order.setCode(code);
        if(status != null)
            order.setStatus(status);
        if(payType != null)
            order.setPayType(payType);
        if(channelId != null)
            order.setChannelId(channelId);
        if(productId != null)
            order.setProductId(productId);



        List<Range<JhiOrder>> ranges = new ArrayList();
        Range<JhiOrder> orderDateRange = new Range<>("orderDate", DateUtil.getStartOfDay(startDate), DateUtil.getEndOfDay(endDate));
        ranges.add(orderDateRange);

        List<JhiOrder> orders = jhiOrderService.findAllByOrderDateBetween(order, ranges);
        new ExcelUtil().renderMergedOutputModel(request, response, orders);
    }


}
