package com.songzi.channel.repository;

import com.songzi.channel.domain.JhiOrder;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.time.LocalDate;
import java.util.List;


/**
 * Spring Data JPA repository for the JhiOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JhiOrderRepository extends JpaRepository<JhiOrder, Long> {

    Page<JhiOrder> findAllByOrderDateBetween(LocalDate startDate, LocalDate endDate, Example order, Pageable pageable);

    List<JhiOrder> findAllByOrderDateBetween(LocalDate startDate, LocalDate endDate, Example order);
}
