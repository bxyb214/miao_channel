package com.songzi.channel.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.songzi.channel.domain.Persona;

import com.songzi.channel.domain.enumeration.PersonaType;
import com.songzi.channel.repository.PersonaRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.*;



import java.util.List;

/**
 * REST controller for managing Persona.
 */
@RestController
@RequestMapping("/api")
@Api(value = "用户渠道", description = "已测：用户画像")
public class PersonaResource {

    private final Logger log = LoggerFactory.getLogger(PersonaResource.class);

    private final PersonaRepository personaRepository;

    public PersonaResource(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }


    /**
     * GET  /personas : get all the personas.
     * @param type the personaType
     * @return the ResponseEntity with status 200 (OK) and the list of personas in body
     */
    @GetMapping("/personas")
    @Timed
    @ApiOperation(value = "已测：获取用户画像")
    public List<Persona> getAllPersonas(
        @ApiParam(value = "类型", required = true) @RequestParam PersonaType type,
        @ApiParam(value = "产品id, 1") @RequestParam(required = false) Long productId,
        @ApiParam(value = "渠道id, 1") @RequestParam(required = false)  Long channelId) {
        log.debug("REST request to get all Personas");

        Persona persona = new Persona();
        if (channelId != null)
            persona.setChannelId(channelId);
        if (productId != null)
            persona.setProductId(productId);
        persona.setPersonaType(type);
        return personaRepository.findAll(Example.of(persona));
    }
}
