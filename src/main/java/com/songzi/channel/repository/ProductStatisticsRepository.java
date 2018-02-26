package com.songzi.channel.repository;

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
public interface ProductStatisticsRepository extends JpaRepository<ProductStatistics, Long> {

    Page<ProductStatistics> findAll(Pageable pageable);

    ProductStatistics findOneByName(String productName);
}

