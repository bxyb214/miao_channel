package com.songzi.channel.web.rest;



import com.codahale.metrics.annotation.Timed;
import com.pingplusplus.model.Customs;
import com.pingplusplus.model.Event;
import com.pingplusplus.model.Webhooks;
import com.songzi.channel.repository.VisitRepository;
import com.songzi.channel.service.JhiOrderService;
import com.songzi.channel.service.VisitService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;

/**
 * REST controller for managing Visit.
 */
@RestController
@RequestMapping("/api/open")
@Api(value = "访问接口", description = "用于测试界面每次打开时调用")
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
    public void visit(HttpServletRequest request, @RequestParam String productName, String channelName) {

        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isEmpty(ip)){
            ip = request.getRemoteAddr();
        }
        visitService.count(ip, productName, channelName);
    }

    @ApiOperation(value = "ping++ 回调接口")
    @GetMapping("/order/{id}/pay")
    @Timed
    public Customs payOrder(HttpServletRequest request, @PathVariable Long orderId, @RequestParam String payType) {

        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isEmpty(ip)){
            ip = request.getRemoteAddr();
        }
        return orderService.tryToPay(orderId, payType, ip);
    }

    @ApiOperation(value = "ping++ 回调接口")
    @GetMapping("/order/webhook")
    @Timed
    public void exportAllJhiOrders(HttpServletRequest request, HttpServletResponse response) throws IOException {

        request.setCharacterEncoding("UTF8");
        //获取头部所有信息
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            System.out.println(key+" "+value);
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
        Event event = Webhooks.eventParse(buffer.toString());
        if ("charge.succeeded".equals(event.getType())) {
            orderService.updateOrderByHook(event);
        }
    }
}

