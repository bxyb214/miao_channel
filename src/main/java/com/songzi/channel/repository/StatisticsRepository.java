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

    @Query(value = "update Statistics s set s.count = s.count+1 where s.type = ?1 and s.date = ?2")
    void plusOneByStatisticsTypeAndDate(StatisticsType type, LocalDate now);

    @Query(value = "update Statistics s set s.count = s.count+1 where s.type = ?1")
    void plusOneByStatisticsType(StatisticsType type);

    @Query(value = "select s from Statistics s where s.type = ?1 and s.date = ?2")
    Statistics findOneByTypeAndDate(StatisticsType uvTotal, LocalDate now);

    Statistics findOneByType(StatisticsType type);

    @Query(value = "update Statistics s set s.count = ?1 where s.type = ?2")
    void updateCountByType(int count, StatisticsType type);

    @Query(value = "update Statistics s set s.count = ?1 where s.type = ?2 and s.date = ?3")
    void updateCountByTypeAndDate(int count, StatisticsType type, LocalDate date);

    @Query(value = "update Statistics s set s.count = ?1 where s.type = ?2 and s.name = ?3")
    void updateCountByTypeAndName(int count, StatisticsType type, String name);

    @Query(value = "select s.count from Statistics s where s.type = ?1")
    int getCountByType(StatisticsType type);

    @Query(value = "select s.count from Statistics s where s.type = ?1 and s.date = ?2")
    int getCountByTypeAndDate(StatisticsType type, LocalDate date);

    @Query(value = "select s.count from Statistics s where s.type = ?1 and s.name = ?2")
    int getCountByTypeAndName(StatisticsType type, String name);

    @Query(value = "select s.count from Statistics s where s.type = ?1 and s.name = ?2 and s.date = ?3")
    int getCountByTypeAndNameAndDate(StatisticsType productSalesMonthly, String productName, LocalDate date);

    Statistics findOneByTypeAndName(StatisticsType channelSales, String name);

    void deleteAllByType(StatisticsType type);
}
