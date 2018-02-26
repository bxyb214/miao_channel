package com.songzi.channel.repository;

import com.songzi.channel.domain.ChannelStatistics;
import com.songzi.channel.domain.ProductStatistics;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data JPA repository for the ProductStatistics entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChannelStatisticsRepository extends JpaRepository<ChannelStatistics, Long> {

    Page<ChannelStatistics> findAllByChannelId(Long channelId, Pageable pageable);
}

