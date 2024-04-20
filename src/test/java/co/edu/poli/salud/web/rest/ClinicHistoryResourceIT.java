package co.edu.poli.salud.web.rest;

import static co.edu.poli.salud.domain.ClinicHistoryAsserts.*;
import static co.edu.poli.salud.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.edu.poli.salud.IntegrationTest;
import co.edu.poli.salud.domain.ClinicHistory;
import co.edu.poli.salud.domain.People;
import co.edu.poli.salud.domain.TypeDiseases;
import co.edu.poli.salud.repository.ClinicHistoryRepository;
import co.edu.poli.salud.service.ClinicHistoryService;
import co.edu.poli.salud.service.dto.ClinicHistoryDTO;
import co.edu.poli.salud.service.mapper.ClinicHistoryMapper;
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
 * Integration tests for the {@link ClinicHistoryResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ClinicHistoryResourceIT {

    private static final LocalDate DEFAULT_DATE_CLINIC = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CLINIC = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/clinic-histories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ClinicHistoryRepository clinicHistoryRepository;

    @Mock
    private ClinicHistoryRepository clinicHistoryRepositoryMock;

    @Autowired
    private ClinicHistoryMapper clinicHistoryMapper;

    @Mock
    private ClinicHistoryService clinicHistoryServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClinicHistoryMockMvc;

    private ClinicHistory clinicHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClinicHistory createEntity(EntityManager em) {
        ClinicHistory clinicHistory = new ClinicHistory().dateClinic(DEFAULT_DATE_CLINIC);
        // Add required entity
        TypeDiseases typeDiseases;
        if (TestUtil.findAll(em, TypeDiseases.class).isEmpty()) {
            typeDiseases = TypeDiseasesResourceIT.createEntity(em);
            em.persist(typeDiseases);
            em.flush();
        } else {
            typeDiseases = TestUtil.findAll(em, TypeDiseases.class).get(0);
        }
        clinicHistory.setTypeDisease(typeDiseases);
        // Add required entity
        People people;
        if (TestUtil.findAll(em, People.class).isEmpty()) {
            people = PeopleResourceIT.createEntity(em);
            em.persist(people);
            em.flush();
        } else {
            people = TestUtil.findAll(em, People.class).get(0);
        }
        clinicHistory.setPeople(people);
        return clinicHistory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClinicHistory createUpdatedEntity(EntityManager em) {
        ClinicHistory clinicHistory = new ClinicHistory().dateClinic(UPDATED_DATE_CLINIC);
        // Add required entity
        TypeDiseases typeDiseases;
        if (TestUtil.findAll(em, TypeDiseases.class).isEmpty()) {
            typeDiseases = TypeDiseasesResourceIT.createUpdatedEntity(em);
            em.persist(typeDiseases);
            em.flush();
        } else {
            typeDiseases = TestUtil.findAll(em, TypeDiseases.class).get(0);
        }
        clinicHistory.setTypeDisease(typeDiseases);
        // Add required entity
        People people;
        if (TestUtil.findAll(em, People.class).isEmpty()) {
            people = PeopleResourceIT.createUpdatedEntity(em);
            em.persist(people);
            em.flush();
        } else {
            people = TestUtil.findAll(em, People.class).get(0);
        }
        clinicHistory.setPeople(people);
        return clinicHistory;
    }

    @BeforeEach
    public void initTest() {
        clinicHistory = createEntity(em);
    }

    @Test
    @Transactional
    void createClinicHistory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ClinicHistory
        ClinicHistoryDTO clinicHistoryDTO = clinicHistoryMapper.toDto(clinicHistory);
        var returnedClinicHistoryDTO = om.readValue(
            restClinicHistoryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clinicHistoryDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ClinicHistoryDTO.class
        );

        // Validate the ClinicHistory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedClinicHistory = clinicHistoryMapper.toEntity(returnedClinicHistoryDTO);
        assertClinicHistoryUpdatableFieldsEquals(returnedClinicHistory, getPersistedClinicHistory(returnedClinicHistory));
    }

    @Test
    @Transactional
    void createClinicHistoryWithExistingId() throws Exception {
        // Create the ClinicHistory with an existing ID
        clinicHistory.setId(1L);
        ClinicHistoryDTO clinicHistoryDTO = clinicHistoryMapper.toDto(clinicHistory);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClinicHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clinicHistoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ClinicHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateClinicIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        clinicHistory.setDateClinic(null);

        // Create the ClinicHistory, which fails.
        ClinicHistoryDTO clinicHistoryDTO = clinicHistoryMapper.toDto(clinicHistory);

        restClinicHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clinicHistoryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllClinicHistories() throws Exception {
        // Initialize the database
        clinicHistoryRepository.saveAndFlush(clinicHistory);

        // Get all the clinicHistoryList
        restClinicHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(clinicHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateClinic").value(hasItem(DEFAULT_DATE_CLINIC.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllClinicHistoriesWithEagerRelationshipsIsEnabled() throws Exception {
        when(clinicHistoryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restClinicHistoryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(clinicHistoryServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllClinicHistoriesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(clinicHistoryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restClinicHistoryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(clinicHistoryRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getClinicHistory() throws Exception {
        // Initialize the database
        clinicHistoryRepository.saveAndFlush(clinicHistory);

        // Get the clinicHistory
        restClinicHistoryMockMvc
            .perform(get(ENTITY_API_URL_ID, clinicHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(clinicHistory.getId().intValue()))
            .andExpect(jsonPath("$.dateClinic").value(DEFAULT_DATE_CLINIC.toString()));
    }

    @Test
    @Transactional
    void getNonExistingClinicHistory() throws Exception {
        // Get the clinicHistory
        restClinicHistoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingClinicHistory() throws Exception {
        // Initialize the database
        clinicHistoryRepository.saveAndFlush(clinicHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the clinicHistory
        ClinicHistory updatedClinicHistory = clinicHistoryRepository.findById(clinicHistory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedClinicHistory are not directly saved in db
        em.detach(updatedClinicHistory);
        updatedClinicHistory.dateClinic(UPDATED_DATE_CLINIC);
        ClinicHistoryDTO clinicHistoryDTO = clinicHistoryMapper.toDto(updatedClinicHistory);

        restClinicHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, clinicHistoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(clinicHistoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the ClinicHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedClinicHistoryToMatchAllProperties(updatedClinicHistory);
    }

    @Test
    @Transactional
    void putNonExistingClinicHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        clinicHistory.setId(longCount.incrementAndGet());

        // Create the ClinicHistory
        ClinicHistoryDTO clinicHistoryDTO = clinicHistoryMapper.toDto(clinicHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClinicHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, clinicHistoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(clinicHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClinicHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchClinicHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        clinicHistory.setId(longCount.incrementAndGet());

        // Create the ClinicHistory
        ClinicHistoryDTO clinicHistoryDTO = clinicHistoryMapper.toDto(clinicHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClinicHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(clinicHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClinicHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamClinicHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        clinicHistory.setId(longCount.incrementAndGet());

        // Create the ClinicHistory
        ClinicHistoryDTO clinicHistoryDTO = clinicHistoryMapper.toDto(clinicHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClinicHistoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clinicHistoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ClinicHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateClinicHistoryWithPatch() throws Exception {
        // Initialize the database
        clinicHistoryRepository.saveAndFlush(clinicHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the clinicHistory using partial update
        ClinicHistory partialUpdatedClinicHistory = new ClinicHistory();
        partialUpdatedClinicHistory.setId(clinicHistory.getId());

        restClinicHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClinicHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedClinicHistory))
            )
            .andExpect(status().isOk());

        // Validate the ClinicHistory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertClinicHistoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedClinicHistory, clinicHistory),
            getPersistedClinicHistory(clinicHistory)
        );
    }

    @Test
    @Transactional
    void fullUpdateClinicHistoryWithPatch() throws Exception {
        // Initialize the database
        clinicHistoryRepository.saveAndFlush(clinicHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the clinicHistory using partial update
        ClinicHistory partialUpdatedClinicHistory = new ClinicHistory();
        partialUpdatedClinicHistory.setId(clinicHistory.getId());

        partialUpdatedClinicHistory.dateClinic(UPDATED_DATE_CLINIC);

        restClinicHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClinicHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedClinicHistory))
            )
            .andExpect(status().isOk());

        // Validate the ClinicHistory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertClinicHistoryUpdatableFieldsEquals(partialUpdatedClinicHistory, getPersistedClinicHistory(partialUpdatedClinicHistory));
    }

    @Test
    @Transactional
    void patchNonExistingClinicHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        clinicHistory.setId(longCount.incrementAndGet());

        // Create the ClinicHistory
        ClinicHistoryDTO clinicHistoryDTO = clinicHistoryMapper.toDto(clinicHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClinicHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, clinicHistoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(clinicHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClinicHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchClinicHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        clinicHistory.setId(longCount.incrementAndGet());

        // Create the ClinicHistory
        ClinicHistoryDTO clinicHistoryDTO = clinicHistoryMapper.toDto(clinicHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClinicHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(clinicHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClinicHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamClinicHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        clinicHistory.setId(longCount.incrementAndGet());

        // Create the ClinicHistory
        ClinicHistoryDTO clinicHistoryDTO = clinicHistoryMapper.toDto(clinicHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClinicHistoryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(clinicHistoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ClinicHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteClinicHistory() throws Exception {
        // Initialize the database
        clinicHistoryRepository.saveAndFlush(clinicHistory);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the clinicHistory
        restClinicHistoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, clinicHistory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return clinicHistoryRepository.count();
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

    protected ClinicHistory getPersistedClinicHistory(ClinicHistory clinicHistory) {
        return clinicHistoryRepository.findById(clinicHistory.getId()).orElseThrow();
    }

    protected void assertPersistedClinicHistoryToMatchAllProperties(ClinicHistory expectedClinicHistory) {
        assertClinicHistoryAllPropertiesEquals(expectedClinicHistory, getPersistedClinicHistory(expectedClinicHistory));
    }

    protected void assertPersistedClinicHistoryToMatchUpdatableProperties(ClinicHistory expectedClinicHistory) {
        assertClinicHistoryAllUpdatablePropertiesEquals(expectedClinicHistory, getPersistedClinicHistory(expectedClinicHistory));
    }
}
