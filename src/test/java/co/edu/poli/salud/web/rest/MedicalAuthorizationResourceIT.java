package co.edu.poli.salud.web.rest;

import static co.edu.poli.salud.domain.MedicalAuthorizationAsserts.*;
import static co.edu.poli.salud.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.edu.poli.salud.IntegrationTest;
import co.edu.poli.salud.domain.ClinicHistory;
import co.edu.poli.salud.domain.MedicalAuthorization;
import co.edu.poli.salud.domain.enumeration.State;
import co.edu.poli.salud.repository.MedicalAuthorizationRepository;
import co.edu.poli.salud.service.MedicalAuthorizationService;
import co.edu.poli.salud.service.dto.MedicalAuthorizationDTO;
import co.edu.poli.salud.service.mapper.MedicalAuthorizationMapper;
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
 * Integration tests for the {@link MedicalAuthorizationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MedicalAuthorizationResourceIT {

    private static final String DEFAULT_DETAIL_AUTHORIZATION = "AAAAAAAAAA";
    private static final String UPDATED_DETAIL_AUTHORIZATION = "BBBBBBBBBB";

    private static final State DEFAULT_STATE_AUTHORIZATION = State.ACTIVE;
    private static final State UPDATED_STATE_AUTHORIZATION = State.INACTIVE;

    private static final LocalDate DEFAULT_DATE_AUTHORIZATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_AUTHORIZATION = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/medical-authorizations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MedicalAuthorizationRepository medicalAuthorizationRepository;

    @Mock
    private MedicalAuthorizationRepository medicalAuthorizationRepositoryMock;

    @Autowired
    private MedicalAuthorizationMapper medicalAuthorizationMapper;

    @Mock
    private MedicalAuthorizationService medicalAuthorizationServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMedicalAuthorizationMockMvc;

    private MedicalAuthorization medicalAuthorization;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MedicalAuthorization createEntity(EntityManager em) {
        MedicalAuthorization medicalAuthorization = new MedicalAuthorization()
            .detailAuthorization(DEFAULT_DETAIL_AUTHORIZATION)
            .stateAuthorization(DEFAULT_STATE_AUTHORIZATION)
            .dateAuthorization(DEFAULT_DATE_AUTHORIZATION);
        // Add required entity
        ClinicHistory clinicHistory;
        if (TestUtil.findAll(em, ClinicHistory.class).isEmpty()) {
            clinicHistory = ClinicHistoryResourceIT.createEntity(em);
            em.persist(clinicHistory);
            em.flush();
        } else {
            clinicHistory = TestUtil.findAll(em, ClinicHistory.class).get(0);
        }
        medicalAuthorization.setClinicHistory(clinicHistory);
        return medicalAuthorization;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MedicalAuthorization createUpdatedEntity(EntityManager em) {
        MedicalAuthorization medicalAuthorization = new MedicalAuthorization()
            .detailAuthorization(UPDATED_DETAIL_AUTHORIZATION)
            .stateAuthorization(UPDATED_STATE_AUTHORIZATION)
            .dateAuthorization(UPDATED_DATE_AUTHORIZATION);
        // Add required entity
        ClinicHistory clinicHistory;
        if (TestUtil.findAll(em, ClinicHistory.class).isEmpty()) {
            clinicHistory = ClinicHistoryResourceIT.createUpdatedEntity(em);
            em.persist(clinicHistory);
            em.flush();
        } else {
            clinicHistory = TestUtil.findAll(em, ClinicHistory.class).get(0);
        }
        medicalAuthorization.setClinicHistory(clinicHistory);
        return medicalAuthorization;
    }

    @BeforeEach
    public void initTest() {
        medicalAuthorization = createEntity(em);
    }

    @Test
    @Transactional
    void createMedicalAuthorization() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MedicalAuthorization
        MedicalAuthorizationDTO medicalAuthorizationDTO = medicalAuthorizationMapper.toDto(medicalAuthorization);
        var returnedMedicalAuthorizationDTO = om.readValue(
            restMedicalAuthorizationMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicalAuthorizationDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MedicalAuthorizationDTO.class
        );

        // Validate the MedicalAuthorization in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMedicalAuthorization = medicalAuthorizationMapper.toEntity(returnedMedicalAuthorizationDTO);
        assertMedicalAuthorizationUpdatableFieldsEquals(
            returnedMedicalAuthorization,
            getPersistedMedicalAuthorization(returnedMedicalAuthorization)
        );
    }

    @Test
    @Transactional
    void createMedicalAuthorizationWithExistingId() throws Exception {
        // Create the MedicalAuthorization with an existing ID
        medicalAuthorization.setId(1L);
        MedicalAuthorizationDTO medicalAuthorizationDTO = medicalAuthorizationMapper.toDto(medicalAuthorization);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMedicalAuthorizationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicalAuthorizationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MedicalAuthorization in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDetailAuthorizationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medicalAuthorization.setDetailAuthorization(null);

        // Create the MedicalAuthorization, which fails.
        MedicalAuthorizationDTO medicalAuthorizationDTO = medicalAuthorizationMapper.toDto(medicalAuthorization);

        restMedicalAuthorizationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicalAuthorizationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStateAuthorizationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medicalAuthorization.setStateAuthorization(null);

        // Create the MedicalAuthorization, which fails.
        MedicalAuthorizationDTO medicalAuthorizationDTO = medicalAuthorizationMapper.toDto(medicalAuthorization);

        restMedicalAuthorizationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicalAuthorizationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateAuthorizationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medicalAuthorization.setDateAuthorization(null);

        // Create the MedicalAuthorization, which fails.
        MedicalAuthorizationDTO medicalAuthorizationDTO = medicalAuthorizationMapper.toDto(medicalAuthorization);

        restMedicalAuthorizationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicalAuthorizationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMedicalAuthorizations() throws Exception {
        // Initialize the database
        medicalAuthorizationRepository.saveAndFlush(medicalAuthorization);

        // Get all the medicalAuthorizationList
        restMedicalAuthorizationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medicalAuthorization.getId().intValue())))
            .andExpect(jsonPath("$.[*].detailAuthorization").value(hasItem(DEFAULT_DETAIL_AUTHORIZATION)))
            .andExpect(jsonPath("$.[*].stateAuthorization").value(hasItem(DEFAULT_STATE_AUTHORIZATION.toString())))
            .andExpect(jsonPath("$.[*].dateAuthorization").value(hasItem(DEFAULT_DATE_AUTHORIZATION.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMedicalAuthorizationsWithEagerRelationshipsIsEnabled() throws Exception {
        when(medicalAuthorizationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMedicalAuthorizationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(medicalAuthorizationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMedicalAuthorizationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(medicalAuthorizationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMedicalAuthorizationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(medicalAuthorizationRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getMedicalAuthorization() throws Exception {
        // Initialize the database
        medicalAuthorizationRepository.saveAndFlush(medicalAuthorization);

        // Get the medicalAuthorization
        restMedicalAuthorizationMockMvc
            .perform(get(ENTITY_API_URL_ID, medicalAuthorization.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(medicalAuthorization.getId().intValue()))
            .andExpect(jsonPath("$.detailAuthorization").value(DEFAULT_DETAIL_AUTHORIZATION))
            .andExpect(jsonPath("$.stateAuthorization").value(DEFAULT_STATE_AUTHORIZATION.toString()))
            .andExpect(jsonPath("$.dateAuthorization").value(DEFAULT_DATE_AUTHORIZATION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingMedicalAuthorization() throws Exception {
        // Get the medicalAuthorization
        restMedicalAuthorizationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMedicalAuthorization() throws Exception {
        // Initialize the database
        medicalAuthorizationRepository.saveAndFlush(medicalAuthorization);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medicalAuthorization
        MedicalAuthorization updatedMedicalAuthorization = medicalAuthorizationRepository
            .findById(medicalAuthorization.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedMedicalAuthorization are not directly saved in db
        em.detach(updatedMedicalAuthorization);
        updatedMedicalAuthorization
            .detailAuthorization(UPDATED_DETAIL_AUTHORIZATION)
            .stateAuthorization(UPDATED_STATE_AUTHORIZATION)
            .dateAuthorization(UPDATED_DATE_AUTHORIZATION);
        MedicalAuthorizationDTO medicalAuthorizationDTO = medicalAuthorizationMapper.toDto(updatedMedicalAuthorization);

        restMedicalAuthorizationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medicalAuthorizationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicalAuthorizationDTO))
            )
            .andExpect(status().isOk());

        // Validate the MedicalAuthorization in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMedicalAuthorizationToMatchAllProperties(updatedMedicalAuthorization);
    }

    @Test
    @Transactional
    void putNonExistingMedicalAuthorization() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalAuthorization.setId(longCount.incrementAndGet());

        // Create the MedicalAuthorization
        MedicalAuthorizationDTO medicalAuthorizationDTO = medicalAuthorizationMapper.toDto(medicalAuthorization);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicalAuthorizationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medicalAuthorizationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicalAuthorizationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MedicalAuthorization in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMedicalAuthorization() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalAuthorization.setId(longCount.incrementAndGet());

        // Create the MedicalAuthorization
        MedicalAuthorizationDTO medicalAuthorizationDTO = medicalAuthorizationMapper.toDto(medicalAuthorization);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicalAuthorizationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicalAuthorizationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MedicalAuthorization in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMedicalAuthorization() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalAuthorization.setId(longCount.incrementAndGet());

        // Create the MedicalAuthorization
        MedicalAuthorizationDTO medicalAuthorizationDTO = medicalAuthorizationMapper.toDto(medicalAuthorization);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicalAuthorizationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicalAuthorizationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MedicalAuthorization in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMedicalAuthorizationWithPatch() throws Exception {
        // Initialize the database
        medicalAuthorizationRepository.saveAndFlush(medicalAuthorization);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medicalAuthorization using partial update
        MedicalAuthorization partialUpdatedMedicalAuthorization = new MedicalAuthorization();
        partialUpdatedMedicalAuthorization.setId(medicalAuthorization.getId());

        partialUpdatedMedicalAuthorization.dateAuthorization(UPDATED_DATE_AUTHORIZATION);

        restMedicalAuthorizationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedicalAuthorization.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMedicalAuthorization))
            )
            .andExpect(status().isOk());

        // Validate the MedicalAuthorization in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMedicalAuthorizationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMedicalAuthorization, medicalAuthorization),
            getPersistedMedicalAuthorization(medicalAuthorization)
        );
    }

    @Test
    @Transactional
    void fullUpdateMedicalAuthorizationWithPatch() throws Exception {
        // Initialize the database
        medicalAuthorizationRepository.saveAndFlush(medicalAuthorization);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medicalAuthorization using partial update
        MedicalAuthorization partialUpdatedMedicalAuthorization = new MedicalAuthorization();
        partialUpdatedMedicalAuthorization.setId(medicalAuthorization.getId());

        partialUpdatedMedicalAuthorization
            .detailAuthorization(UPDATED_DETAIL_AUTHORIZATION)
            .stateAuthorization(UPDATED_STATE_AUTHORIZATION)
            .dateAuthorization(UPDATED_DATE_AUTHORIZATION);

        restMedicalAuthorizationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedicalAuthorization.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMedicalAuthorization))
            )
            .andExpect(status().isOk());

        // Validate the MedicalAuthorization in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMedicalAuthorizationUpdatableFieldsEquals(
            partialUpdatedMedicalAuthorization,
            getPersistedMedicalAuthorization(partialUpdatedMedicalAuthorization)
        );
    }

    @Test
    @Transactional
    void patchNonExistingMedicalAuthorization() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalAuthorization.setId(longCount.incrementAndGet());

        // Create the MedicalAuthorization
        MedicalAuthorizationDTO medicalAuthorizationDTO = medicalAuthorizationMapper.toDto(medicalAuthorization);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicalAuthorizationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, medicalAuthorizationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(medicalAuthorizationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MedicalAuthorization in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMedicalAuthorization() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalAuthorization.setId(longCount.incrementAndGet());

        // Create the MedicalAuthorization
        MedicalAuthorizationDTO medicalAuthorizationDTO = medicalAuthorizationMapper.toDto(medicalAuthorization);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicalAuthorizationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(medicalAuthorizationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MedicalAuthorization in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMedicalAuthorization() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalAuthorization.setId(longCount.incrementAndGet());

        // Create the MedicalAuthorization
        MedicalAuthorizationDTO medicalAuthorizationDTO = medicalAuthorizationMapper.toDto(medicalAuthorization);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicalAuthorizationMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(medicalAuthorizationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MedicalAuthorization in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMedicalAuthorization() throws Exception {
        // Initialize the database
        medicalAuthorizationRepository.saveAndFlush(medicalAuthorization);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the medicalAuthorization
        restMedicalAuthorizationMockMvc
            .perform(delete(ENTITY_API_URL_ID, medicalAuthorization.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return medicalAuthorizationRepository.count();
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

    protected MedicalAuthorization getPersistedMedicalAuthorization(MedicalAuthorization medicalAuthorization) {
        return medicalAuthorizationRepository.findById(medicalAuthorization.getId()).orElseThrow();
    }

    protected void assertPersistedMedicalAuthorizationToMatchAllProperties(MedicalAuthorization expectedMedicalAuthorization) {
        assertMedicalAuthorizationAllPropertiesEquals(
            expectedMedicalAuthorization,
            getPersistedMedicalAuthorization(expectedMedicalAuthorization)
        );
    }

    protected void assertPersistedMedicalAuthorizationToMatchUpdatableProperties(MedicalAuthorization expectedMedicalAuthorization) {
        assertMedicalAuthorizationAllUpdatablePropertiesEquals(
            expectedMedicalAuthorization,
            getPersistedMedicalAuthorization(expectedMedicalAuthorization)
        );
    }
}
