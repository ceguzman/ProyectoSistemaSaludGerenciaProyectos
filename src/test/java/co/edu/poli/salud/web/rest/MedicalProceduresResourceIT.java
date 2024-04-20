package co.edu.poli.salud.web.rest;

import static co.edu.poli.salud.domain.MedicalProceduresAsserts.*;
import static co.edu.poli.salud.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.edu.poli.salud.IntegrationTest;
import co.edu.poli.salud.domain.MedicalAuthorization;
import co.edu.poli.salud.domain.MedicalProcedures;
import co.edu.poli.salud.repository.MedicalProceduresRepository;
import co.edu.poli.salud.service.MedicalProceduresService;
import co.edu.poli.salud.service.dto.MedicalProceduresDTO;
import co.edu.poli.salud.service.mapper.MedicalProceduresMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MedicalProceduresResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MedicalProceduresResourceIT {

    private static final String DEFAULT_TYPE_PROCEDURES = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_PROCEDURES = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_PROCEDURES = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_PROCEDURES = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/medical-procedures";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MedicalProceduresRepository medicalProceduresRepository;

    @Mock
    private MedicalProceduresRepository medicalProceduresRepositoryMock;

    @Autowired
    private MedicalProceduresMapper medicalProceduresMapper;

    @Mock
    private MedicalProceduresService medicalProceduresServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMedicalProceduresMockMvc;

    private MedicalProcedures medicalProcedures;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MedicalProcedures createEntity(EntityManager em) {
        MedicalProcedures medicalProcedures = new MedicalProcedures()
            .typeProcedures(DEFAULT_TYPE_PROCEDURES)
            .description(DEFAULT_DESCRIPTION)
            .dateProcedures(DEFAULT_DATE_PROCEDURES);
        // Add required entity
        MedicalAuthorization medicalAuthorization;
        if (TestUtil.findAll(em, MedicalAuthorization.class).isEmpty()) {
            medicalAuthorization = MedicalAuthorizationResourceIT.createEntity(em);
            em.persist(medicalAuthorization);
            em.flush();
        } else {
            medicalAuthorization = TestUtil.findAll(em, MedicalAuthorization.class).get(0);
        }
        medicalProcedures.setMedicalAuthorization(medicalAuthorization);
        return medicalProcedures;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MedicalProcedures createUpdatedEntity(EntityManager em) {
        MedicalProcedures medicalProcedures = new MedicalProcedures()
            .typeProcedures(UPDATED_TYPE_PROCEDURES)
            .description(UPDATED_DESCRIPTION)
            .dateProcedures(UPDATED_DATE_PROCEDURES);
        // Add required entity
        MedicalAuthorization medicalAuthorization;
        if (TestUtil.findAll(em, MedicalAuthorization.class).isEmpty()) {
            medicalAuthorization = MedicalAuthorizationResourceIT.createUpdatedEntity(em);
            em.persist(medicalAuthorization);
            em.flush();
        } else {
            medicalAuthorization = TestUtil.findAll(em, MedicalAuthorization.class).get(0);
        }
        medicalProcedures.setMedicalAuthorization(medicalAuthorization);
        return medicalProcedures;
    }

    @BeforeEach
    public void initTest() {
        medicalProcedures = createEntity(em);
    }

    @Test
    @Transactional
    void createMedicalProcedures() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MedicalProcedures
        MedicalProceduresDTO medicalProceduresDTO = medicalProceduresMapper.toDto(medicalProcedures);
        var returnedMedicalProceduresDTO = om.readValue(
            restMedicalProceduresMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicalProceduresDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MedicalProceduresDTO.class
        );

        // Validate the MedicalProcedures in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMedicalProcedures = medicalProceduresMapper.toEntity(returnedMedicalProceduresDTO);
        assertMedicalProceduresUpdatableFieldsEquals(returnedMedicalProcedures, getPersistedMedicalProcedures(returnedMedicalProcedures));
    }

    @Test
    @Transactional
    void createMedicalProceduresWithExistingId() throws Exception {
        // Create the MedicalProcedures with an existing ID
        medicalProcedures.setId(1L);
        MedicalProceduresDTO medicalProceduresDTO = medicalProceduresMapper.toDto(medicalProcedures);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMedicalProceduresMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicalProceduresDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MedicalProcedures in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTypeProceduresIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medicalProcedures.setTypeProcedures(null);

        // Create the MedicalProcedures, which fails.
        MedicalProceduresDTO medicalProceduresDTO = medicalProceduresMapper.toDto(medicalProcedures);

        restMedicalProceduresMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicalProceduresDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medicalProcedures.setDescription(null);

        // Create the MedicalProcedures, which fails.
        MedicalProceduresDTO medicalProceduresDTO = medicalProceduresMapper.toDto(medicalProcedures);

        restMedicalProceduresMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicalProceduresDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateProceduresIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medicalProcedures.setDateProcedures(null);

        // Create the MedicalProcedures, which fails.
        MedicalProceduresDTO medicalProceduresDTO = medicalProceduresMapper.toDto(medicalProcedures);

        restMedicalProceduresMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicalProceduresDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMedicalProcedures() throws Exception {
        // Initialize the database
        medicalProceduresRepository.saveAndFlush(medicalProcedures);

        // Get all the medicalProceduresList
        restMedicalProceduresMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medicalProcedures.getId().intValue())))
            .andExpect(jsonPath("$.[*].typeProcedures").value(hasItem(DEFAULT_TYPE_PROCEDURES)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].dateProcedures").value(hasItem(DEFAULT_DATE_PROCEDURES.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMedicalProceduresWithEagerRelationshipsIsEnabled() throws Exception {
        when(medicalProceduresServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMedicalProceduresMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(medicalProceduresServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMedicalProceduresWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(medicalProceduresServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMedicalProceduresMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(medicalProceduresRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getMedicalProcedures() throws Exception {
        // Initialize the database
        medicalProceduresRepository.saveAndFlush(medicalProcedures);

        // Get the medicalProcedures
        restMedicalProceduresMockMvc
            .perform(get(ENTITY_API_URL_ID, medicalProcedures.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(medicalProcedures.getId().intValue()))
            .andExpect(jsonPath("$.typeProcedures").value(DEFAULT_TYPE_PROCEDURES))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.dateProcedures").value(DEFAULT_DATE_PROCEDURES.toString()));
    }

    @Test
    @Transactional
    void getNonExistingMedicalProcedures() throws Exception {
        // Get the medicalProcedures
        restMedicalProceduresMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMedicalProcedures() throws Exception {
        // Initialize the database
        medicalProceduresRepository.saveAndFlush(medicalProcedures);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medicalProcedures
        MedicalProcedures updatedMedicalProcedures = medicalProceduresRepository.findById(medicalProcedures.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMedicalProcedures are not directly saved in db
        em.detach(updatedMedicalProcedures);
        updatedMedicalProcedures
            .typeProcedures(UPDATED_TYPE_PROCEDURES)
            .description(UPDATED_DESCRIPTION)
            .dateProcedures(UPDATED_DATE_PROCEDURES);
        MedicalProceduresDTO medicalProceduresDTO = medicalProceduresMapper.toDto(updatedMedicalProcedures);

        restMedicalProceduresMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medicalProceduresDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicalProceduresDTO))
            )
            .andExpect(status().isOk());

        // Validate the MedicalProcedures in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMedicalProceduresToMatchAllProperties(updatedMedicalProcedures);
    }

    @Test
    @Transactional
    void putNonExistingMedicalProcedures() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalProcedures.setId(longCount.incrementAndGet());

        // Create the MedicalProcedures
        MedicalProceduresDTO medicalProceduresDTO = medicalProceduresMapper.toDto(medicalProcedures);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicalProceduresMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medicalProceduresDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicalProceduresDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MedicalProcedures in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMedicalProcedures() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalProcedures.setId(longCount.incrementAndGet());

        // Create the MedicalProcedures
        MedicalProceduresDTO medicalProceduresDTO = medicalProceduresMapper.toDto(medicalProcedures);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicalProceduresMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicalProceduresDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MedicalProcedures in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMedicalProcedures() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalProcedures.setId(longCount.incrementAndGet());

        // Create the MedicalProcedures
        MedicalProceduresDTO medicalProceduresDTO = medicalProceduresMapper.toDto(medicalProcedures);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicalProceduresMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicalProceduresDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MedicalProcedures in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMedicalProceduresWithPatch() throws Exception {
        // Initialize the database
        medicalProceduresRepository.saveAndFlush(medicalProcedures);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medicalProcedures using partial update
        MedicalProcedures partialUpdatedMedicalProcedures = new MedicalProcedures();
        partialUpdatedMedicalProcedures.setId(medicalProcedures.getId());

        partialUpdatedMedicalProcedures
            .typeProcedures(UPDATED_TYPE_PROCEDURES)
            .description(UPDATED_DESCRIPTION)
            .dateProcedures(UPDATED_DATE_PROCEDURES);

        restMedicalProceduresMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedicalProcedures.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMedicalProcedures))
            )
            .andExpect(status().isOk());

        // Validate the MedicalProcedures in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMedicalProceduresUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMedicalProcedures, medicalProcedures),
            getPersistedMedicalProcedures(medicalProcedures)
        );
    }

    @Test
    @Transactional
    void fullUpdateMedicalProceduresWithPatch() throws Exception {
        // Initialize the database
        medicalProceduresRepository.saveAndFlush(medicalProcedures);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medicalProcedures using partial update
        MedicalProcedures partialUpdatedMedicalProcedures = new MedicalProcedures();
        partialUpdatedMedicalProcedures.setId(medicalProcedures.getId());

        partialUpdatedMedicalProcedures
            .typeProcedures(UPDATED_TYPE_PROCEDURES)
            .description(UPDATED_DESCRIPTION)
            .dateProcedures(UPDATED_DATE_PROCEDURES);

        restMedicalProceduresMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedicalProcedures.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMedicalProcedures))
            )
            .andExpect(status().isOk());

        // Validate the MedicalProcedures in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMedicalProceduresUpdatableFieldsEquals(
            partialUpdatedMedicalProcedures,
            getPersistedMedicalProcedures(partialUpdatedMedicalProcedures)
        );
    }

    @Test
    @Transactional
    void patchNonExistingMedicalProcedures() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalProcedures.setId(longCount.incrementAndGet());

        // Create the MedicalProcedures
        MedicalProceduresDTO medicalProceduresDTO = medicalProceduresMapper.toDto(medicalProcedures);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicalProceduresMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, medicalProceduresDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(medicalProceduresDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MedicalProcedures in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMedicalProcedures() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalProcedures.setId(longCount.incrementAndGet());

        // Create the MedicalProcedures
        MedicalProceduresDTO medicalProceduresDTO = medicalProceduresMapper.toDto(medicalProcedures);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicalProceduresMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(medicalProceduresDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MedicalProcedures in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMedicalProcedures() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalProcedures.setId(longCount.incrementAndGet());

        // Create the MedicalProcedures
        MedicalProceduresDTO medicalProceduresDTO = medicalProceduresMapper.toDto(medicalProcedures);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicalProceduresMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(medicalProceduresDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MedicalProcedures in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMedicalProcedures() throws Exception {
        // Initialize the database
        medicalProceduresRepository.saveAndFlush(medicalProcedures);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the medicalProcedures
        restMedicalProceduresMockMvc
            .perform(delete(ENTITY_API_URL_ID, medicalProcedures.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return medicalProceduresRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected MedicalProcedures getPersistedMedicalProcedures(MedicalProcedures medicalProcedures) {
        return medicalProceduresRepository.findById(medicalProcedures.getId()).orElseThrow();
    }

    protected void assertPersistedMedicalProceduresToMatchAllProperties(MedicalProcedures expectedMedicalProcedures) {
        assertMedicalProceduresAllPropertiesEquals(expectedMedicalProcedures, getPersistedMedicalProcedures(expectedMedicalProcedures));
    }

    protected void assertPersistedMedicalProceduresToMatchUpdatableProperties(MedicalProcedures expectedMedicalProcedures) {
        assertMedicalProceduresAllUpdatablePropertiesEquals(
            expectedMedicalProcedures,
            getPersistedMedicalProcedures(expectedMedicalProcedures)
        );
    }
}
