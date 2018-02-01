package com.songzi.channel.repository;

import com.songzi.channel.domain.Persona;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the Persona entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {

    List<Persona> findAllByPersonaType(String type);

}
