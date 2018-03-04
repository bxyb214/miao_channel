package com.songzi.channel.repository;

import com.songzi.channel.domain.JhiOrder;
import com.songzi.channel.domain.enumeration.OrderStatus;
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

    @Query(value = "select o from JhiOrder o where o.orderDate = ?1 and o.status = ?2")
    List<JhiOrder> findAllByDateAndStatus(LocalDate date, OrderStatus status);

    JhiOrder findOneByCode(String code);

    @Query(value = "select count(o) from JhiOrder o where o.channelId = ?1 and o.orderDate = ?2")
    int countByChannelIdAndDate(Long id, LocalDate date);

    @Query(value = "select count(o) from JhiOrder o where o.channelId = ?1 and o.orderDate = ?2 and o.status = ?3")
    int countByChannelIdAndDateAndStatus(Long id, LocalDate date, OrderStatus status);

    @Query(value = "select count(o) from JhiOrder o where o.orderDate = ?1 and o.status = ?2")
    int countByDateAndStatus(LocalDate date, OrderStatus 已支付);

    @Query(value = "select coalesce(sum(o.price), 0) from jhi_order o where o.channel_id = ?1 and o.order_date = ?2 and o.status = ?3", nativeQuery = true)
    double sumPriceByChannelIdAndDateAndStatus(Long id, LocalDate yesterday, OrderStatus status);

    @Query(value = "select coalesce(sum(o.proportion_price), 0.0) from jhi_order o where o.channel_id = ?1 and o.order_date = ?2 and o.status = ?3", nativeQuery = true)
    double sumProportionPriceByChannelIdAndDateAndStatus(Long id, LocalDate yesterday, OrderStatus normal);

    @Query(value = "select coalesce(sum(o.price), 0.0) from jhi_order o where o.order_date = ?1 and o.status = ?2", nativeQuery = true)
    double sumByDateAndStatus(LocalDate date, OrderStatus status);

    @Query(value = "select coalesce(sum(o.price), 0.0) from jhi_order o where o.order_date = ?1 and o.channel_id = ?2 and o.product_id = ?3 and  o.status = ?4", nativeQuery = true)
    double sumPriceByDateAndChannelIdAndProductIdAndStatus(LocalDate date, Long channelId, Long productId,  OrderStatus status);

    @Query(value = "select coalesce(sum(o.price), 0.0) from jhi_order o where o.order_date = ?1 and o.channel_id = ?2 and  o.status = ?3", nativeQuery = true)
    double sumPriceByDateAndChannelIdAndStatus(LocalDate date, Long channelId, OrderStatus status);

    @Query(value = "select coalesce(sum(o.price), 0.0) from jhi_order o where o.order_date = ?1 and o.product_id = ?2 and  o.status = ?3", nativeQuery = true)
    double sumPriceByDateAndProductIdAndStatus(LocalDate date, Long productId, OrderStatus status);

    List<JhiOrder> findAllByStatus(OrderStatus status);
}
