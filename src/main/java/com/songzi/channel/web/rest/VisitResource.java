package com.songzi.channel.web.rest;



import com.songzi.channel.repository.VisitRepository;
import com.songzi.channel.service.VisitService;

import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.*;

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
}

