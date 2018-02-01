package com.songzi.channel.repository;

import com.songzi.channel.domain.Statistics;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Statistics entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StatisticsRepository extends JpaRepository<Statistics, Long> {

    Page<Statistics> findAllByType(Pageable pageable);
}
