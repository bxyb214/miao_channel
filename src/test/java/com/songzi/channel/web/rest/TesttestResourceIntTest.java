package com.songzi.channel.web.rest;

import com.songzi.channel.ChannelApp;

import com.songzi.channel.domain.Testtest;
import com.songzi.channel.repository.TesttestRepository;
import com.songzi.channel.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.songzi.channel.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TesttestResource REST controller.
 *
 * @see TesttestResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChannelApp.class)
public class TesttestResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private TesttestRepository testtestRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTesttestMockMvc;

    private Testtest testtest;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TesttestResource testtestResource = new TesttestResource(testtestRepository);
        this.restTesttestMockMvc = MockMvcBuilders.standaloneSetup(testtestResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Testtest createEntity(EntityManager em) {
        Testtest testtest = new Testtest()
            .name(DEFAULT_NAME);
        return testtest;
    }

    @Before
    public void initTest() {
        testtest = createEntity(em);
    }

    @Test
    @Transactional
    public void createTesttest() throws Exception {
        int databaseSizeBeforeCreate = testtestRepository.findAll().size();

        // Create the Testtest
        restTesttestMockMvc.perform(post("/api/testtests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(testtest)))
            .andExpect(status().isCreated());

        // Validate the Testtest in the database
        List<Testtest> testtestList = testtestRepository.findAll();
        assertThat(testtestList).hasSize(databaseSizeBeforeCreate + 1);
        Testtest testTesttest = testtestList.get(testtestList.size() - 1);
        assertThat(testTesttest.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createTesttestWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = testtestRepository.findAll().size();

        // Create the Testtest with an existing ID
        testtest.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTesttestMockMvc.perform(post("/api/testtests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(testtest)))
            .andExpect(status().isBadRequest());

        // Validate the Testtest in the database
        List<Testtest> testtestList = testtestRepository.findAll();
        assertThat(testtestList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = testtestRepository.findAll().size();
        // set the field null
        testtest.setName(null);

        // Create the Testtest, which fails.

        restTesttestMockMvc.perform(post("/api/testtests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(testtest)))
            .andExpect(status().isBadRequest());

        List<Testtest> testtestList = testtestRepository.findAll();
        assertThat(testtestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTesttests() throws Exception {
        // Initialize the database
        testtestRepository.saveAndFlush(testtest);

        // Get all the testtestList
        restTesttestMockMvc.perform(get("/api/testtests?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testtest.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getTesttest() throws Exception {
        // Initialize the database
        testtestRepository.saveAndFlush(testtest);

        // Get the testtest
        restTesttestMockMvc.perform(get("/api/testtests/{id}", testtest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(testtest.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTesttest() throws Exception {
        // Get the testtest
        restTesttestMockMvc.perform(get("/api/testtests/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTesttest() throws Exception {
        // Initialize the database
        testtestRepository.saveAndFlush(testtest);
        int databaseSizeBeforeUpdate = testtestRepository.findAll().size();

        // Update the testtest
        Testtest updatedTesttest = testtestRepository.findOne(testtest.getId());
        // Disconnect from session so that the updates on updatedTesttest are not directly saved in db
        em.detach(updatedTesttest);
        updatedTesttest
            .name(UPDATED_NAME);

        restTesttestMockMvc.perform(put("/api/testtests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTesttest)))
            .andExpect(status().isOk());

        // Validate the Testtest in the database
        List<Testtest> testtestList = testtestRepository.findAll();
        assertThat(testtestList).hasSize(databaseSizeBeforeUpdate);
        Testtest testTesttest = testtestList.get(testtestList.size() - 1);
        assertThat(testTesttest.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingTesttest() throws Exception {
        int databaseSizeBeforeUpdate = testtestRepository.findAll().size();

        // Create the Testtest

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTesttestMockMvc.perform(put("/api/testtests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(testtest)))
            .andExpect(status().isCreated());

        // Validate the Testtest in the database
        List<Testtest> testtestList = testtestRepository.findAll();
        assertThat(testtestList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTesttest() throws Exception {
        // Initialize the database
        testtestRepository.saveAndFlush(testtest);
        int databaseSizeBeforeDelete = testtestRepository.findAll().size();

        // Get the testtest
        restTesttestMockMvc.perform(delete("/api/testtests/{id}", testtest.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Testtest> testtestList = testtestRepository.findAll();
        assertThat(testtestList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Testtest.class);
        Testtest testtest1 = new Testtest();
        testtest1.setId(1L);
        Testtest testtest2 = new Testtest();
        testtest2.setId(testtest1.getId());
        assertThat(testtest1).isEqualTo(testtest2);
        testtest2.setId(2L);
        assertThat(testtest1).isNotEqualTo(testtest2);
        testtest1.setId(null);
        assertThat(testtest1).isNotEqualTo(testtest2);
    }
}
