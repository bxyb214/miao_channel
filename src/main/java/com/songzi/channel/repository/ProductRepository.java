package com.songzi.channel.repository;

import com.songzi.channel.domain.Channel;
import com.songzi.channel.domain.Product;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the Product entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findAll(Example product, Pageable pageable);

    @EntityGraph(attributePaths = "channels")
    Product findOneWithChannelsById(Long id);

    @EntityGraph(attributePaths = "channels")
    Page<Product> findAllByChannels_Id(Long channelId, Pageable pageable);

    @EntityGraph(attributePaths = "channels")
    @Query(value = "select p from Product p join p.channels c")
    List<Product> findAllProductChannelCode();

    Product findOneByCode(String productCode);
}
