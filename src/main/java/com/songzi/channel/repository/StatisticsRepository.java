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

    List<Statistics> findAllByTypeAndDateBetweenOrderByDateAsc(StatisticsType type, LocalDate start, LocalDate end);

    @Query(value = "select sum(s.count) from Statistics s where s.type = ?1 and s.date between ?2 and ?3")
    double getSumByTypeAndDateBetween(StatisticsType type, LocalDate start, LocalDate end);

    @Query(value = "select sum(s.count) from Statistics s where s.type = ?1 and s.date between ?2 and ?3 and s.productCode = ?4 and s.channelCode = ?5")
    double getSumByTypeAndDateBetweenAndProductCodeAndChannelCode(StatisticsType type, LocalDate start, LocalDate end, String productCode, String channelCode);


    List<Statistics> findAllByTypeOrderByCountAsc(StatisticsType type);

    @Query(value = "update Statistics s set s.count = s.count+1 where s.type = ?1 and s.date = ?2")
    void plusOneByStatisticsTypeAndDate(StatisticsType type, LocalDate now);

    @Query(value = "update Statistics s set s.count = s.count+1 where s.type = ?1")
    void plusOneByStatisticsType(StatisticsType type);

    @Query(value = "select s from Statistics s where s.type = ?1 and s.date = ?2")
    Statistics findOneByTypeAndDate(StatisticsType uvTotal, LocalDate now);

    Statistics findOneByType(StatisticsType type);

    @Query(value = "update Statistics s set s.count = ?1 where s.type = ?2")
    void updateCountByType(Double count, StatisticsType type);

    @Query(value = "update Statistics s set s.count = ?1 where s.type = ?2 and s.date = ?3")
    void updateCountByTypeAndDate(Double count, StatisticsType type, LocalDate date);

    @Query(value = "update Statistics s set s.count = ?1 where s.type = ?2 and s.name = ?3")
    void updateCountByTypeAndName(Double count, StatisticsType type, String name);

    @Query(value = "select count(s) from Statistics s where s.type = ?1")
    int countByType(StatisticsType type);

    @Query(value = "select count from statistics s where s.statistics_type = ?1 and s.jhi_date = ?2", nativeQuery = true)
    Object getCountByTypeAndDate(StatisticsType type, LocalDate date);

    @Query(value = "select count from statistics s where statistics_type = ?1 and name = ?2", nativeQuery = true)
    Object getCountByTypeAndName(StatisticsType type, String name);

    @Query(value = "select count from statistics s where statistics_type = ?1 and name = ?2 and jhi_date = ?3", nativeQuery = true)
    Object getCountByTypeAndNameAndDate(StatisticsType type, String name, LocalDate date);

    @Query(value = "select count from statistics where statistics_type = ?1 and jhi_date = ?2 and channel_code = ?3 and product_code = ?4 ", nativeQuery = true)
    Object getCountByTypeAndDateAndProductCodeAndChannelCode(StatisticsType type, LocalDate date, String productCode, String channelCode);

    Statistics findOneByTypeAndName(StatisticsType channelSales, String name);

    void deleteAllByType(StatisticsType type);

    List<Statistics> findAllByTypeAndDate(StatisticsType type, LocalDate data);

    @Query(value = "select s from Statistics s where s.type = ?1 and s.channelCode = ?2")
    List<Statistics> findAllByTypeAndChannelCode(StatisticsType type, String channelCode);

    @Query(value = "select s from Statistics s where s.type = ?1 and s.productCode = ?2 and s.channelCode = ?3")
    Statistics findOneByTypeAndProductCodeAndChannelCode(StatisticsType salesProductChannelTotal, String productCode, String channelCode);

    @Query(value = "select s from Statistics s where s.type = ?1 and s.date = ?2 and s.productCode = ?3 and s.channelCode = ?4")
    Statistics findOneByTypeAndDateAndProductCodeAndChannelCode(StatisticsType type, LocalDate date, String productCode, String channelCode);

    List<Statistics> findAllByUpdateDate(LocalDate date);
}
