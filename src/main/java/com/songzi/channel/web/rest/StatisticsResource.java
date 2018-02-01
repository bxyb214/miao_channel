package com.songzi.channel.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.songzi.channel.domain.Statistics;

import com.songzi.channel.repository.StatisticsRepository;
import com.songzi.channel.web.rest.errors.BadRequestAlertException;
import com.songzi.channel.web.rest.util.HeaderUtil;
import com.songzi.channel.web.rest.util.PaginationUtil;
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

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Statistics.
 */
@RestController
@RequestMapping("/api")
@Api(value = "总体分析", description = "总体分析")
public class StatisticsResource {

    private final Logger log = LoggerFactory.getLogger(StatisticsResource.class);

    private static final String ENTITY_NAME = "statistics";

    private final StatisticsRepository statisticsRepository;

    public StatisticsResource(StatisticsRepository statisticsRepository) {
        this.statisticsRepository = statisticsRepository;
    }




    /**
     * GET  /statistics : get all the statistics.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of statistics in body
     */
    @GetMapping("/statistics")
    @Timed
    @ApiOperation(value = "热门测试排行")
    public ResponseEntity<List<Statistics>> getAllStatistics(Pageable pageable, @RequestParam String type) {
        log.debug("REST request to get a page of Statistics");
        Page<Statistics> page = statisticsRepository.findAllByType(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/statistics");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
