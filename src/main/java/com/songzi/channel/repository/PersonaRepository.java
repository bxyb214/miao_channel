package com.songzi.channel.repository;

import com.songzi.channel.domain.Persona;
import com.songzi.channel.domain.enumeration.PersonaType;
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

    @Query(value = "Select p from Persona p where p.name = ?1 and p.personaType = ?2 and p.channelId = ?3 and p.productId = ?4")
    Persona findOneByNameAndTypeAndChannelIdAndProductId(String city, PersonaType type, Long channelId, Long productId);
}
