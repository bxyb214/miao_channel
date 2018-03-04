package com.songzi.channel.web.rest;


import com.codahale.metrics.annotation.Timed;
import com.pingplusplus.model.Customs;
import com.pingplusplus.model.Event;
import com.pingplusplus.model.Webhooks;
import com.songzi.channel.domain.JhiOrder;
import com.songzi.channel.security.AuthoritiesConstants;
import com.songzi.channel.service.JhiOrderService;
import com.songzi.channel.service.ScheduleService;
import com.songzi.channel.service.VisitService;
import com.songzi.channel.web.rest.vm.OrderVM;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;

/**
 * REST controller for managing Visit.
 */
@RestController
@RequestMapping("/api/test")
@Api(value = "测试接口", description = "测试接口，仅限管理员使用")
public class TestResource {

    private final Logger log = LoggerFactory.getLogger(TestResource.class);

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final ScheduleService scheduleService;

    public TestResource(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping("/schedule")
    @Timed
    @ApiOperation(value = "定时任务接口")
//    @Secured(AuthoritiesConstants.ADMIN)
    public void visit(@ApiParam(value = "日期，2018-01-01") @RequestParam String dateStr) {
        LocalDate date = LocalDate.parse(dateStr, formatter);
        scheduleService.dailyTask(date);
    }
}

