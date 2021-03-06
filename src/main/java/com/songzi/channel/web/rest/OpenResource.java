package com.songzi.channel.web.rest;



import com.codahale.metrics.annotation.Timed;
import com.pingplusplus.model.Charge;
import com.pingplusplus.model.Customs;
import com.pingplusplus.model.Event;
import com.pingplusplus.model.Webhooks;
import com.songzi.channel.domain.JhiOrder;
import com.songzi.channel.service.JhiOrderService;
import com.songzi.channel.service.VisitService;

import com.songzi.channel.web.rest.vm.OrderVM;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Optional;

/**
 * REST controller for managing Visit.
 */
@RestController
@RequestMapping("/api/open")
@Api(value = "开放接口", description = "用于测试H5访问")
public class OpenResource {

    private final Logger log = LoggerFactory.getLogger(OpenResource.class);

    private static final String ENTITY_NAME = "visit";


    private final VisitService visitService;

    private final JhiOrderService orderService;

    public OpenResource(VisitService visitService,
                        JhiOrderService orderService) {
        this.orderService = orderService;
        this.visitService = visitService;
    }

    /**
     * @return a string list of the all of the roles
     */
    @GetMapping("visit/count")
    @Timed
    @ApiOperation(value = "已测：浏览时调用接口")
    public void visit(HttpServletRequest request,
                      @ApiParam(value = "产品code") @RequestParam String p,
                      @ApiParam(value = "测试Code") @RequestParam String c) {

        String ip = getIp(request);
        log.info("visit {}, {}, {}", ip, p, c);
        visitService.count(ip, p, c);
    }

    /**
     * GET  /channels/:id : get the "id" channel.
     *
     * @param orderNo the code of the order to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the channel, or with status 404 (Not Found)
     */
    @GetMapping("orders/{orderNo}")
    @Timed
    @ApiOperation(value = "已测；订单详情")
    public ResponseEntity<JhiOrder> getChannel(@PathVariable String orderNo) {
        log.debug("REST request to get Channel : {}", orderNo);
        JhiOrder order = orderService.findOneByCode(orderNo);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(order));
    }

    /**
     * POST  /orders : Create a new channel.
     *
     * @param
     * @return the ResponseEntity with status 201 (Created) and with body the new channel, or with status 400 (Bad Request) if the channel has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/orders")
    @Timed
    @ApiOperation(value = "已测; 创建订单")
    public ResponseEntity<JhiOrder> createOrder(HttpServletRequest request, @RequestBody OrderVM orderVM) {
        log.debug("REST request to save orderVM : {}", orderVM);
        String ip = getIp(request);
        JhiOrder order = orderService.save(orderVM, ip);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @ApiOperation(value = "ping++ 回调接口")
    @PostMapping("/orders/{orderNo}/pay/{payType}")
    @Timed
    public Charge payOrder(HttpServletRequest request,
                           @PathVariable String orderNo,
                           @PathVariable String payType,
                           @RequestBody(required = false) HashMap<String, String> extra) {

        String ip = getIp(request);
        return orderService.tryToPay(orderNo, payType, extra, ip);
    }

    @ApiOperation(value = "ping++ 回调接口")
    @PostMapping("/orders/webhook")
    @Timed
    public void webhook(HttpServletRequest request) throws IOException {

        request.setCharacterEncoding("UTF8");
        //获取头部所有信息
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            log.debug(key+" "+value);
        }
        // 获得 http body 内容
        BufferedReader reader = request.getReader();
        StringBuffer buffer = new StringBuffer();
        String string;
        while ((string = reader.readLine()) != null) {
            buffer.append(string);
        }
        reader.close();
        // 解析异步通知数据
        String eventStr = buffer.toString();
        log.debug("webhook body = " + eventStr);
        Event event = Webhooks.eventParse(eventStr);
        if ("charge.succeeded".equals(event.getType())) {
            orderService.updateOrderByHook(event);
        }
    }

    private static String getIp(HttpServletRequest request){

        String ip = request.getHeader("X-Real-IP");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}

