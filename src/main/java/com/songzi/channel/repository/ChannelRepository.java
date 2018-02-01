package com.songzi.channel.repository;

import com.songzi.channel.domain.Channel;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.Optional;


/**
 * Spring Data JPA repository for the Channel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChannelRepository extends JpaRepository<Channel, Long> {

    Channel findOneByUserId(Long userId);

    Channel findOneById(String id);

    Page<Channel> findAll(Example channel, Pageable pageable);
}
