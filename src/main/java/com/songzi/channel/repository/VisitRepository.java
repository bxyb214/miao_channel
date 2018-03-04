package com.songzi.channel.repository;

import com.songzi.channel.domain.Visit;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;


/**
 * Spring Data JPA repository for the Visit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {
    Visit findOneByIpAndDate(String ip, LocalDate date);

    @Query(value = "SELECT DISTINCT v.ip FROM Visit v WHERE v.date = ?1")
    Set<String> findDistinctIpByDate(LocalDate date);

    @Query(value = "SELECT count(v) FROM Visit v WHERE v.date = ?1 and v.productCode = ?2 and v.channelCode = ?3")
    Integer countByDateAndProductCodeAndChannelCode( LocalDate date, String productCode, String channelCode);

    @Query(value = "SELECT count(distinct v.ip) FROM Visit v WHERE v.date = ?1 and v.productCode = ?2 and v.channelCode = ?3")
    Integer countDistinctByDateAndProductCodeAndChannelCode(LocalDate yesterday, String s, String code);

    @Query(value = "SELECT count(distinct v.ip) FROM Visit v WHERE v.date = ?1")
    Integer countDistinctByDate(LocalDate date);

    @Query(value = "SELECT count(v) FROM Visit v WHERE v.date = ?1")
    Integer countByDate(LocalDate date);
}
