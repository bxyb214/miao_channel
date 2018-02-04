package com.songzi.channel.repository;

import com.songzi.channel.domain.Statistics;
import com.songzi.channel.domain.enumeration.StatisticsType;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

/**
 * Spring Data JPA repository for the Statistics entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StatisticsRepository extends JpaRepository<Statistics, Long> {

    List<Statistics> findAllByType(StatisticsType type);

//    @Query(value = "select s from Statistics s where s.type = ?1 and s.date between ?2 and ?3")
    List<Statistics> findAllByTypeAndDateBetween(StatisticsType type, LocalDate start, LocalDate end);

    List<Statistics> findAllByTypeOrderByCountAsc(StatisticsType type);
}
