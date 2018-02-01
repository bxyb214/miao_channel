package com.songzi.channel.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.songzi.channel.domain.Channel;

import com.songzi.channel.repository.ChannelRepository;
import com.songzi.channel.service.ChannelService;
import com.songzi.channel.web.rest.errors.BadRequestAlertException;
import com.songzi.channel.web.rest.util.HeaderUtil;
import com.songzi.channel.web.rest.util.PaginationUtil;
import com.songzi.channel.web.rest.vm.ChannelVM;
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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Channel.
 */
@RestController
@RequestMapping("/api")
@Api(value = "渠道管理", description = "渠道管理")
public class ChannelResource {

    private final Logger log = LoggerFactory.getLogger(ChannelResource.class);

    private static final String ENTITY_NAME = "channel";

    private final ChannelRepository channelRepository;

    private final ChannelService channelService;

    public ChannelResource(ChannelRepository channelRepository, ChannelService channelService) {
        this.channelRepository = channelRepository;
        this.channelService = channelService;
    }

    /**
     * POST  /channels : Create a new channel.
     *
     * @param channelVM the channel to create
     * @return the ResponseEntity with status 201 (Created) and with body the new channel, or with status 400 (Bad Request) if the channel has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/channels")
    @Timed
    @ApiOperation(value = "创建渠道")
    public ResponseEntity<Channel> createChannel(@Valid @RequestBody ChannelVM channelVM) throws URISyntaxException {
        log.debug("REST request to save Channel : {}", channelVM);
        if (channelVM.getChannel().getId() != null) {
            throw new BadRequestAlertException("A new channel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Channel result = channelService.save(channelVM);
        return ResponseEntity.created(new URI("/api/channels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /channels : Updates an existing channel.
     *
     * @param channelVM the channel to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated channel,
     * or with status 400 (Bad Request) if the channel is not valid,
     * or with status 500 (Internal Server Error) if the channel couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/channels")
    @Timed
    @ApiOperation(value = "更新渠道")
    public ResponseEntity<Channel> updateChannel(@Valid @RequestBody ChannelVM channelVM) throws URISyntaxException {
        log.debug("REST request to update Channel : {}", channelVM);
        if (channelVM.getChannel().getId() == null) {
            return createChannel(channelVM);
        }
        Channel result = channelService.update(channelVM);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * GET  /channels : get all the channels.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of channels in body
     */
    @GetMapping("/channels")
    @Timed
    @ApiOperation(value = "渠道列表")
    public ResponseEntity<List<Channel>> getAllChannels(Pageable pageable, @RequestBody Channel channel) {
        log.debug("REST request to get a page of Channels");
        Page<Channel> page = channelRepository.findAll(Example.of(channel), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/channels");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /channels/:id : get the "id" channel.
     *
     * @param id the id of the channel to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the channel, or with status 404 (Not Found)
     */
    @GetMapping("/channels/{id}")
    @Timed
    @ApiOperation(value = "渠道详情")
    public ResponseEntity<Channel> getChannel(@PathVariable Long id) {
        log.debug("REST request to get Channel : {}", id);
        Channel channel = channelRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(channel));
    }

    /**
     * DELETE  /channels/:id : delete the "id" channel.
     *
     * @param id the id of the channel to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/channels/{id}")
    @Timed
    @ApiOperation(value = "删除渠道")
    public ResponseEntity<Void> deleteChannel(@PathVariable Long id) {
        log.debug("REST request to delete Channel : {}", id);
        channelRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
