package com.songzi.channel.service;

import com.songzi.channel.domain.JhiOrder;
import com.songzi.channel.repository.JhiOrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


/**
 * Service Implementation for managing JhiOrder.
 */
@Service
@Transactional
public class JhiOrderService {

    private final Logger log = LoggerFactory.getLogger(JhiOrderService.class);

    private final JhiOrderRepository jhiOrderRepository;

    public JhiOrderService(JhiOrderRepository jhiOrderRepository) {
        this.jhiOrderRepository = jhiOrderRepository;
    }

    /**
     * Save a jhi_order.
     *
     * @param jhiOrder the entity to save
     * @return the persisted entity
     */
    public JhiOrder save(JhiOrder jhiOrder) {
        log.debug("Request to save JhiOrder : {}", jhiOrder);
        return jhiOrderRepository.save(jhiOrder);
    }

    /**
     * Get all the jhi_orders.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<JhiOrder> findAll(Pageable pageable) {
        log.debug("Request to get all Jhi_orders");
        return jhiOrderRepository.findAll(pageable);
    }

    /**
     * Get one jhi_order by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public JhiOrder findOne(Long id) {
        log.debug("Request to get JhiOrder : {}", id);
        return jhiOrderRepository.findOne(id);
    }

    /**
     * Delete the jhi_order by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete JhiOrder : {}", id);
        jhiOrderRepository.delete(id);
    }

    public Page<JhiOrder> queryByExampleByDateBetween(Example order, Pageable pageable, LocalDate startDate, LocalDate endDate) {

       return jhiOrderRepository.findAllByOrderDateBetween(startDate, endDate, order, pageable);
    }

    public List<JhiOrder> queryByExampleByDateBetween(Example order, LocalDate startDate, LocalDate endDate) {
        return jhiOrderRepository.findAllByOrderDateBetween(startDate, endDate, order);
        }
}
