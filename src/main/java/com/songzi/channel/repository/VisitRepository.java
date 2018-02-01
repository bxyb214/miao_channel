package com.songzi.channel.repository;

import com.songzi.channel.domain.Visit;
import com.songzi.channel.service.dto.ChannelSalesPriceDTO;
import com.songzi.channel.service.dto.PVDTO;
import com.songzi.channel.service.dto.SalesNumberDTO;
import com.songzi.channel.service.dto.SalesPriceDTO;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.time.LocalDate;
import java.util.List;


/**
 * Spring Data JPA repository for the Visit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {

    @Query(value = "select sum(salesPrice) from Visit")
    double getSalePriceTotal();

    Visit findOneByDate(LocalDate date);

    @Query(value = "select v from Visit v where v.date > ?1")
    List<Visit> findByDateAFTER(LocalDate date);

    @Query(value = "select sum(salesNumber) from Visit")
    int getSaleNumberTotal();

    @Query(value = "select sum(pv) from Visit")
    int getPvTotal();

    @Query(value = "select sum(salesPrice) as salesPrice, date from Visit v where v.date between ?1 and ?2 group by v.date")
    List<SalesPriceDTO> getSalesPricesByDateBetween(LocalDate startDate, LocalDate endDate);

    @Query(value = "select sum(pv) as pv, date from Visit v where v.date between ?1 and ?2 group by v.date")
    List<PVDTO> getPVsByDateBetween(LocalDate startDate, LocalDate endDate);

    @Query(value = "select sum(salesNumber) as salesNumber, date from Visit v where v.date between ?1 and ?2 group by v.date")
    List<SalesNumberDTO> getSalesNumbersByDateBetween(LocalDate startDate, LocalDate endDate);

    @Query(value = "select sum(salesPrice) as salesPriceSum, date from Visit v where v.date between ?1 and ?2 group by v.channelId")
    List<ChannelSalesPriceDTO> getChannelsOrderBySalesPrice(LocalDate startDate, LocalDate endDate);
}
