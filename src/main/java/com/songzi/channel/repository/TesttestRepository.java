package com.songzi.channel.repository;

import com.songzi.channel.domain.Testtest;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Testtest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TesttestRepository extends JpaRepository<Testtest, Long> {

}
