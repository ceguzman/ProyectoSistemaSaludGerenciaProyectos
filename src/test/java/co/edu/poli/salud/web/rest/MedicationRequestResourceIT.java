package co.edu.poli.salud.web.rest;

import static co.edu.poli.salud.domain.MedicationRequestAsserts.*;
import static co.edu.poli.salud.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.edu.poli.salud.IntegrationTest;
import co.edu.poli.salud.domain.MedicalAuthorization;
import co.edu.poli.salud.domain.MedicationRequest;
import co.edu.poli.salud.repository.MedicationRequestRepository;
import co.edu.poli.salud.service.MedicationRequestService;
import co.edu.poli.salud.service.dto.MedicationRequestDTO;
import co.edu.poli.salud.service.mapper.MedicationRequestMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link MedicationRequestResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MedicationRequestResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_AMOUNT = 10;
    private static final Integer UPDATED_AMOUNT = 9;

    private static final Integer DEFAULT_MILLIGRAMS = 10;
    private static final Integer UPDATED_MILLIGRAMS = 9;

    private static final String ENTITY_API_URL = "/api/medication-requests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MedicationRequestRepository medicationRequestRepository;

    @Mock
    private MedicationRequestRepository medicationRequestRepositoryMock;

    @Autowired
    private MedicationRequestMapper medicationRequestMapper;

    @Mock
    private MedicationRequestService medicationRequestServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMedicationRequestMockMvc;

    private MedicationRequest medicationRequest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MedicationRequest createEntity(EntityManager em) {
        MedicationRequest medicationRequest = new MedicationRequest()
            .name(DEFAULT_NAME)
            .amount(DEFAULT_AMOUNT)
            .milligrams(DEFAULT_MILLIGRAMS);
        // Add required entity
        MedicalAuthorization medicalAuthorization;
        if (TestUtil.findAll(em, MedicalAuthorization.class).isEmpty()) {
            medicalAuthorization = MedicalAuthorizationResourceIT.createEntity(em);
            em.persist(medicalAuthorization);
            em.flush();
        } else {
            medicalAuthorization = TestUtil.findAll(em, MedicalAuthorization.class).get(0);
        }
        medicationRequest.setMedicalAuthorization(medicalAuthorization);
        return medicationRequest;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MedicationRequest createUpdatedEntity(EntityManager em) {
        MedicationRequest medicationRequest = new MedicationRequest()
            .name(UPDATED_NAME)
            .amount(UPDATED_AMOUNT)
            .milligrams(UPDATED_MILLIGRAMS);
        // Add required entity
        MedicalAuthorization medicalAuthorization;
        if (TestUtil.findAll(em, MedicalAuthorization.class).isEmpty()) {
            medicalAuthorization = MedicalAuthorizationResourceIT.createUpdatedEntity(em);
            em.persist(medicalAuthorization);
            em.flush();
        } else {
            medicalAuthorization = TestUtil.findAll(em, MedicalAuthorization.class).get(0);
        }
        medicationRequest.setMedicalAuthorization(medicalAuthorization);
        return medicationRequest;
    }

    @BeforeEach
    public void initTest() {
        medicationRequest = createEntity(em);
    }

    @Test
    @Transactional
    void createMedicationRequest() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MedicationRequest
        MedicationRequestDTO medicationRequestDTO = medicationRequestMapper.toDto(medicationRequest);
        var returnedMedicationRequestDTO = om.readValue(
            restMedicationRequestMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicationRequestDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MedicationRequestDTO.class
        );

        // Validate the MedicationRequest in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMedicationRequest = medicationRequestMapper.toEntity(returnedMedicationRequestDTO);
        assertMedicationRequestUpdatableFieldsEquals(returnedMedicationRequest, getPersistedMedicationRequest(returnedMedicationRequest));
    }

    @Test
    @Transactional
    void createMedicationRequestWithExistingId() throws Exception {
        // Create the MedicationRequest with an existing ID
        medicationRequest.setId(1L);
        MedicationRequestDTO medicationRequestDTO = medicationRequestMapper.toDto(medicationRequest);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMedicationRequestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicationRequestDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MedicationRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medicationRequest.setName(null);

        // Create the MedicationRequest, which fails.
        MedicationRequestDTO medicationRequestDTO = medicationRequestMapper.toDto(medicationRequest);

        restMedicationRequestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicationRequestDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medicationRequest.setAmount(null);

        // Create the MedicationRequest, which fails.
        MedicationRequestDTO medicationRequestDTO = medicationRequestMapper.toDto(medicationRequest);

        restMedicationRequestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicationRequestDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMedicationRequests() throws Exception {
        // Initialize the database
        medicationRequestRepository.saveAndFlush(medicationRequest);

        // Get all the medicationRequestList
        restMedicationRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medicationRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.[*].milligrams").value(hasItem(DEFAULT_MILLIGRAMS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMedicationRequestsWithEagerRelationshipsIsEnabled() throws Exception {
        when(medicationRequestServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMedicationRequestMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(medicationRequestServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMedicationRequestsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(medicationRequestServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMedicationRequestMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(medicationRequestRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getMedicationRequest() throws Exception {
        // Initialize the database
        medicationRequestRepository.saveAndFlush(medicationRequest);

        // Get the medicationRequest
        restMedicationRequestMockMvc
            .perform(get(ENTITY_API_URL_ID, medicationRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(medicationRequest.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT))
            .andExpect(jsonPath("$.milligrams").value(DEFAULT_MILLIGRAMS));
    }

    @Test
    @Transactional
    void getNonExistingMedicationRequest() throws Exception {
        // Get the medicationRequest
        restMedicationRequestMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMedicationRequest() throws Exception {
        // Initialize the database
        medicationRequestRepository.saveAndFlush(medicationRequest);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medicationRequest
        MedicationRequest updatedMedicationRequest = medicationRequestRepository.findById(medicationRequest.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMedicationRequest are not directly saved in db
        em.detach(updatedMedicationRequest);
        updatedMedicationRequest.name(UPDATED_NAME).amount(UPDATED_AMOUNT).milligrams(UPDATED_MILLIGRAMS);
        MedicationRequestDTO medicationRequestDTO = medicationRequestMapper.toDto(updatedMedicationRequest);

        restMedicationRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medicationRequestDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicationRequestDTO))
            )
            .andExpect(status().isOk());

        // Validate the MedicationRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMedicationRequestToMatchAllProperties(updatedMedicationRequest);
    }

    @Test
    @Transactional
    void putNonExistingMedicationRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicationRequest.setId(longCount.incrementAndGet());

        // Create the MedicationRequest
        MedicationRequestDTO medicationRequestDTO = medicationRequestMapper.toDto(medicationRequest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicationRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medicationRequestDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicationRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MedicationRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMedicationRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicationRequest.setId(longCount.incrementAndGet());

        // Create the MedicationRequest
        MedicationRequestDTO medicationRequestDTO = medicationRequestMapper.toDto(medicationRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicationRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicationRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MedicationRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMedicationRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicationRequest.setId(longCount.incrementAndGet());

        // Create the MedicationRequest
        MedicationRequestDTO medicationRequestDTO = medicationRequestMapper.toDto(medicationRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicationRequestMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicationRequestDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MedicationRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMedicationRequestWithPatch() throws Exception {
        // Initialize the database
        medicationRequestRepository.saveAndFlush(medicationRequest);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medicationRequest using partial update
        MedicationRequest partialUpdatedMedicationRequest = new MedicationRequest();
        partialUpdatedMedicationRequest.setId(medicationRequest.getId());

        partialUpdatedMedicationRequest.name(UPDATED_NAME).amount(UPDATED_AMOUNT);

        restMedicationRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedicationRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMedicationRequest))
            )
            .andExpect(status().isOk());

        // Validate the MedicationRequest in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMedicationRequestUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMedicationRequest, medicationRequest),
            getPersistedMedicationRequest(medicationRequest)
        );
    }

    @Test
    @Transactional
    void fullUpdateMedicationRequestWithPatch() throws Exception {
        // Initialize the database
        medicationRequestRepository.saveAndFlush(medicationRequest);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medicationRequest using partial update
        MedicationRequest partialUpdatedMedicationRequest = new MedicationRequest();
        partialUpdatedMedicationRequest.setId(medicationRequest.getId());

        partialUpdatedMedicationRequest.name(UPDATED_NAME).amount(UPDATED_AMOUNT).milligrams(UPDATED_MILLIGRAMS);

        restMedicationRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedicationRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMedicationRequest))
            )
            .andExpect(status().isOk());

        // Validate the MedicationRequest in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMedicationRequestUpdatableFieldsEquals(
            partialUpdatedMedicationRequest,
            getPersistedMedicationRequest(partialUpdatedMedicationRequest)
        );
    }

    @Test
    @Transactional
    void patchNonExistingMedicationRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicationRequest.setId(longCount.incrementAndGet());

        // Create the MedicationRequest
        MedicationRequestDTO medicationRequestDTO = medicationRequestMapper.toDto(medicationRequest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicationRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, medicationRequestDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(medicationRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MedicationRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMedicationRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicationRequest.setId(longCount.incrementAndGet());

        // Create the MedicationRequest
        MedicationRequestDTO medicationRequestDTO = medicationRequestMapper.toDto(medicationRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicationRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(medicationRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MedicationRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMedicationRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicationRequest.setId(longCount.incrementAndGet());

        // Create the MedicationRequest
        MedicationRequestDTO medicationRequestDTO = medicationRequestMapper.toDto(medicationRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicationRequestMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(medicationRequestDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MedicationRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMedicationRequest() throws Exception {
        // Initialize the database
        medicationRequestRepository.saveAndFlush(medicationRequest);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the medicationRequest
        restMedicationRequestMockMvc
            .perform(delete(ENTITY_API_URL_ID, medicationRequest.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return medicationRequestRepository.count();
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

    protected MedicationRequest getPersistedMedicationRequest(MedicationRequest medicationRequest) {
        return medicationRequestRepository.findById(medicationRequest.getId()).orElseThrow();
    }

    protected void assertPersistedMedicationRequestToMatchAllProperties(MedicationRequest expectedMedicationRequest) {
        assertMedicationRequestAllPropertiesEquals(expectedMedicationRequest, getPersistedMedicationRequest(expectedMedicationRequest));
    }

    protected void assertPersistedMedicationRequestToMatchUpdatableProperties(MedicationRequest expectedMedicationRequest) {
        assertMedicationRequestAllUpdatablePropertiesEquals(
            expectedMedicationRequest,
            getPersistedMedicationRequest(expectedMedicationRequest)
        );
    }
}
