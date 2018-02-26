package com.songzi.channel.web.rest;



import com.codahale.metrics.annotation.Timed;
import com.songzi.channel.repository.VisitRepository;
import com.songzi.channel.service.VisitService;

import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.access.annotation.Secured;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * REST controller for managing Visit.
 */
@RestController
@RequestMapping("/api")
@Api(value = "访问接口", description = "用于测试界面每次打开时调用")
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
     * @return a string list of the all of the roles
     */
    @GetMapping("visit/count")
    @Timed
    public void visit(HttpServletRequest request, @RequestParam Long productId) {

        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isEmpty(ip)){
            ip = request.getRemoteAddr();
        }
        visitService.count(ip, productId + "");

    }

}

