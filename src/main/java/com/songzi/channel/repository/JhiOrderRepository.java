package com.songzi.channel.repository;

import com.songzi.channel.domain.JhiOrder;
import com.songzi.channel.domain.enumeration.Status;
import com.songzi.channel.repository.support.WiselyRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;


/**
 * Spring Data JPA repository for the JhiOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JhiOrderRepository extends WiselyRepository<JhiOrder, Long> {

    Page<JhiOrder> findAllByOrderDateBetween(Instant startDate, Instant endDate, Example order, Pageable pageable);

    List<JhiOrder> findAllByOrderDateBetween(LocalDate startDate, LocalDate endDate, Example order);

    JhiOrder findOneByCode(String orderNo);

    @Query(value = "select count(o) from JhiOrder o where o.channelId = ?1 and o.orderDate = ?2")
    int getCountByChannelIdAndDate(Long id, LocalDate date);

    @Query(value = "select count(o) from JhiOrder o where o.channelId = ?1 and o.orderDate = ?2 and o.status = ?3")
    int getCountByChannelIdAndDateAndStatus(Long id, LocalDate date, Status status);

    @Query(value = "select sum(o.price) from JhiOrder o where o.channelId = ?1 and o.orderDate = ?2 and o.status = ?3")
    int getPriceByChannelIdAndDateAndStatus(Long id, LocalDate yesterday, Status normal);

    @Query(value = "select sum(o.proportionPrice) from JhiOrder o where o.channelId = ?1 and o.orderDate = ?2 and o.status = ?3")
    int getProportionPriceByChannelIdAndDateAndStatus(Long id, LocalDate yesterday, Status normal);
}
