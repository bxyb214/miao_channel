package com.songzi.channel.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.songzi.channel.domain.Testtest;

import com.songzi.channel.repository.TesttestRepository;
import com.songzi.channel.web.rest.errors.BadRequestAlertException;
import com.songzi.channel.web.rest.util.HeaderUtil;
import com.songzi.channel.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
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
 * REST controller for managing Testtest.
 */
@RestController
@RequestMapping("/api")
public class TesttestResource {

    private final Logger log = LoggerFactory.getLogger(TesttestResource.class);

    private static final String ENTITY_NAME = "testtest";

    private final TesttestRepository testtestRepository;

    public TesttestResource(TesttestRepository testtestRepository) {
        this.testtestRepository = testtestRepository;
    }

    /**
     * POST  /testtests : Create a new testtest.
     *
     * @param testtest the testtest to create
     * @return the ResponseEntity with status 201 (Created) and with body the new testtest, or with status 400 (Bad Request) if the testtest has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/testtests")
    @Timed
    public ResponseEntity<Testtest> createTesttest(@Valid @RequestBody Testtest testtest) throws URISyntaxException {
        log.debug("REST request to save Testtest : {}", testtest);
        if (testtest.getId() != null) {
            throw new BadRequestAlertException("A new testtest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Testtest result = testtestRepository.save(testtest);
        return ResponseEntity.created(new URI("/api/testtests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /testtests : Updates an existing testtest.
     *
     * @param testtest the testtest to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated testtest,
     * or with status 400 (Bad Request) if the testtest is not valid,
     * or with status 500 (Internal Server Error) if the testtest couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/testtests")
    @Timed
    public ResponseEntity<Testtest> updateTesttest(@Valid @RequestBody Testtest testtest) throws URISyntaxException {
        log.debug("REST request to update Testtest : {}", testtest);
        if (testtest.getId() == null) {
            return createTesttest(testtest);
        }
        Testtest result = testtestRepository.save(testtest);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, testtest.getId().toString()))
            .body(result);
    }

    /**
     * GET  /testtests : get all the testtests.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of testtests in body
     */
    @GetMapping("/testtests")
    @Timed
    public ResponseEntity<List<Testtest>> getAllTesttests(Pageable pageable) {
        log.debug("REST request to get a page of Testtests");
        Page<Testtest> page = testtestRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/testtests");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /testtests/:id : get the "id" testtest.
     *
     * @param id the id of the testtest to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the testtest, or with status 404 (Not Found)
     */
    @GetMapping("/testtests/{id}")
    @Timed
    public ResponseEntity<Testtest> getTesttest(@PathVariable Long id) {
        log.debug("REST request to get Testtest : {}", id);
        Testtest testtest = testtestRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(testtest));
    }

    /**
     * DELETE  /testtests/:id : delete the "id" testtest.
     *
     * @param id the id of the testtest to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/testtests/{id}")
    @Timed
    public ResponseEntity<Void> deleteTesttest(@PathVariable Long id) {
        log.debug("REST request to delete Testtest : {}", id);
        testtestRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
