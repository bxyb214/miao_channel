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

    Page<JhiOrder> findAllByOrderDateBetween(Instant start, Instant end, Example order, Pageable pageable);

    List<JhiOrder> findAllByOrderDateBetween(Instant start, Instant end, Example order);

    JhiOrder findOneByCode(String code);

    @Query(value = "select o from JhiOrder o where o.status = ?1 and o.orderDate between ?2 and ?3")
    List<JhiOrder> findAllByStatusAndOrderDateBetween(OrderStatus status, Instant start, Instant end);

    @Query(value = "select count(o) from JhiOrder o where o.channelId = ?1 and o.orderDate between ?2 and ?3")
    int countByChannelIdAndOrderDateBetween(Long id, Instant start, Instant end);

    @Query(value = "select count(o) from JhiOrder o where o.channelId = ?1 and o.status = ?2 and o.orderDate between ?3 and ?4")
    int countByChannelIdAndStatusAndOrderDateBetween(Long id, OrderStatus status, Instant start, Instant end);

    @Query(value = "select count(o) from JhiOrder o where  o.status = ?1 and o.orderDate between ?2 and ?3")
    int countByStatusAndOrderDateBetween(OrderStatus status, Instant start, Instant end);

    @Query(value = "select coalesce(sum(o.price), 0) as price from jhi_order o where o.channel_id = ?1 and o.status = ?2 and (o.order_date between ?3 and ?4)", nativeQuery = true)
    Double sumPriceByChannelIdAndStatusAndOrderDateBetween(Long id, String status, Instant start, Instant end);

    @Query(value = "select coalesce(sum(o.proportion_price), 0.0) as price from jhi_order o where o.channel_id = ?1 and o.status = ?2 and (o.order_date between ?3 and ?4)", nativeQuery = true)
    Double sumProportionByPriceByChannelIdAndStatusAndOrderDateBetween(Long id, String status,  Instant start, Instant end);

    @Query(value = "select coalesce(sum(o.price), 0.0) as price from jhi_order o where o.status = ?1 and (o.order_date between ?2 and ?3)", nativeQuery = true)
    Double sumPriceByStatusAndOrderDateBetween(String status, Instant start, Instant end);

    @Query(value = "select coalesce(sum(o.price), 0.0) as price from jhi_order o where o.channel_id = ?1 and o.product_id = ?2 and  o.status = ?3 and (o.order_date between ?4 and ?5)", nativeQuery = true)
    Double sumPriceByChannelIdAndProductIdAndStatusAndOrderDateBetween(Long channelId, Long productId, String status, Instant start, Instant end);

    @Query(value = "select coalesce(sum(o.price), 0.0) as price from jhi_order o where o.product_id = ?1 and  o.status = ?2 and (o.order_date between ?3 and ?4)", nativeQuery = true)
    Double sumPriceByProductIdAndStatusAndOrderDateBetween(Long productId, String status, Instant start, Instant end);

    List<JhiOrder> findAllByStatus(OrderStatus status);

    Integer countByProductIdAndStatus(Long productId, OrderStatus status);

    Integer countByChannelIdAndProductIdAndStatus(Long channelId, Long productId, OrderStatus status);
}
