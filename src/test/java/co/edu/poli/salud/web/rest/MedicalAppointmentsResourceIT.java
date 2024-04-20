package co.edu.poli.salud.web.rest;

import static co.edu.poli.salud.domain.MedicalAppointmentsAsserts.*;
import static co.edu.poli.salud.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.edu.poli.salud.IntegrationTest;
import co.edu.poli.salud.domain.MedicalAppointments;
import co.edu.poli.salud.domain.TypeSpecialist;
import co.edu.poli.salud.repository.MedicalAppointmentsRepository;
import co.edu.poli.salud.service.MedicalAppointmentsService;
import co.edu.poli.salud.service.dto.MedicalAppointmentsDTO;
import co.edu.poli.salud.service.mapper.MedicalAppointmentsMapper;
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
 * Integration tests for the {@link MedicalAppointmentsResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MedicalAppointmentsResourceIT {

    private static final LocalDate DEFAULT_DATE_MEDICAL = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_MEDICAL = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/medical-appointments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MedicalAppointmentsRepository medicalAppointmentsRepository;

    @Mock
    private MedicalAppointmentsRepository medicalAppointmentsRepositoryMock;

    @Autowired
    private MedicalAppointmentsMapper medicalAppointmentsMapper;

    @Mock
    private MedicalAppointmentsService medicalAppointmentsServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMedicalAppointmentsMockMvc;

    private MedicalAppointments medicalAppointments;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MedicalAppointments createEntity(EntityManager em) {
        MedicalAppointments medicalAppointments = new MedicalAppointments().dateMedical(DEFAULT_DATE_MEDICAL);
        // Add required entity
        TypeSpecialist typeSpecialist;
        if (TestUtil.findAll(em, TypeSpecialist.class).isEmpty()) {
            typeSpecialist = TypeSpecialistResourceIT.createEntity(em);
            em.persist(typeSpecialist);
            em.flush();
        } else {
            typeSpecialist = TestUtil.findAll(em, TypeSpecialist.class).get(0);
        }
        medicalAppointments.setTypeSpecialist(typeSpecialist);
        return medicalAppointments;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MedicalAppointments createUpdatedEntity(EntityManager em) {
        MedicalAppointments medicalAppointments = new MedicalAppointments().dateMedical(UPDATED_DATE_MEDICAL);
        // Add required entity
        TypeSpecialist typeSpecialist;
        if (TestUtil.findAll(em, TypeSpecialist.class).isEmpty()) {
            typeSpecialist = TypeSpecialistResourceIT.createUpdatedEntity(em);
            em.persist(typeSpecialist);
            em.flush();
        } else {
            typeSpecialist = TestUtil.findAll(em, TypeSpecialist.class).get(0);
        }
        medicalAppointments.setTypeSpecialist(typeSpecialist);
        return medicalAppointments;
    }

    @BeforeEach
    public void initTest() {
        medicalAppointments = createEntity(em);
    }

    @Test
    @Transactional
    void createMedicalAppointments() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MedicalAppointments
        MedicalAppointmentsDTO medicalAppointmentsDTO = medicalAppointmentsMapper.toDto(medicalAppointments);
        var returnedMedicalAppointmentsDTO = om.readValue(
            restMedicalAppointmentsMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicalAppointmentsDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MedicalAppointmentsDTO.class
        );

        // Validate the MedicalAppointments in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMedicalAppointments = medicalAppointmentsMapper.toEntity(returnedMedicalAppointmentsDTO);
        assertMedicalAppointmentsUpdatableFieldsEquals(
            returnedMedicalAppointments,
            getPersistedMedicalAppointments(returnedMedicalAppointments)
        );
    }

    @Test
    @Transactional
    void createMedicalAppointmentsWithExistingId() throws Exception {
        // Create the MedicalAppointments with an existing ID
        medicalAppointments.setId(1L);
        MedicalAppointmentsDTO medicalAppointmentsDTO = medicalAppointmentsMapper.toDto(medicalAppointments);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMedicalAppointmentsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicalAppointmentsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MedicalAppointments in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateMedicalIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medicalAppointments.setDateMedical(null);

        // Create the MedicalAppointments, which fails.
        MedicalAppointmentsDTO medicalAppointmentsDTO = medicalAppointmentsMapper.toDto(medicalAppointments);

        restMedicalAppointmentsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicalAppointmentsDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMedicalAppointments() throws Exception {
        // Initialize the database
        medicalAppointmentsRepository.saveAndFlush(medicalAppointments);

        // Get all the medicalAppointmentsList
        restMedicalAppointmentsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medicalAppointments.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateMedical").value(hasItem(DEFAULT_DATE_MEDICAL.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMedicalAppointmentsWithEagerRelationshipsIsEnabled() throws Exception {
        when(medicalAppointmentsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMedicalAppointmentsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(medicalAppointmentsServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMedicalAppointmentsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(medicalAppointmentsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMedicalAppointmentsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(medicalAppointmentsRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getMedicalAppointments() throws Exception {
        // Initialize the database
        medicalAppointmentsRepository.saveAndFlush(medicalAppointments);

        // Get the medicalAppointments
        restMedicalAppointmentsMockMvc
            .perform(get(ENTITY_API_URL_ID, medicalAppointments.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(medicalAppointments.getId().intValue()))
            .andExpect(jsonPath("$.dateMedical").value(DEFAULT_DATE_MEDICAL.toString()));
    }

    @Test
    @Transactional
    void getNonExistingMedicalAppointments() throws Exception {
        // Get the medicalAppointments
        restMedicalAppointmentsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMedicalAppointments() throws Exception {
        // Initialize the database
        medicalAppointmentsRepository.saveAndFlush(medicalAppointments);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medicalAppointments
        MedicalAppointments updatedMedicalAppointments = medicalAppointmentsRepository.findById(medicalAppointments.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMedicalAppointments are not directly saved in db
        em.detach(updatedMedicalAppointments);
        updatedMedicalAppointments.dateMedical(UPDATED_DATE_MEDICAL);
        MedicalAppointmentsDTO medicalAppointmentsDTO = medicalAppointmentsMapper.toDto(updatedMedicalAppointments);

        restMedicalAppointmentsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medicalAppointmentsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicalAppointmentsDTO))
            )
            .andExpect(status().isOk());

        // Validate the MedicalAppointments in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMedicalAppointmentsToMatchAllProperties(updatedMedicalAppointments);
    }

    @Test
    @Transactional
    void putNonExistingMedicalAppointments() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalAppointments.setId(longCount.incrementAndGet());

        // Create the MedicalAppointments
        MedicalAppointmentsDTO medicalAppointmentsDTO = medicalAppointmentsMapper.toDto(medicalAppointments);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicalAppointmentsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medicalAppointmentsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicalAppointmentsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MedicalAppointments in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMedicalAppointments() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalAppointments.setId(longCount.incrementAndGet());

        // Create the MedicalAppointments
        MedicalAppointmentsDTO medicalAppointmentsDTO = medicalAppointmentsMapper.toDto(medicalAppointments);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicalAppointmentsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicalAppointmentsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MedicalAppointments in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMedicalAppointments() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalAppointments.setId(longCount.incrementAndGet());

        // Create the MedicalAppointments
        MedicalAppointmentsDTO medicalAppointmentsDTO = medicalAppointmentsMapper.toDto(medicalAppointments);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicalAppointmentsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicalAppointmentsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MedicalAppointments in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMedicalAppointmentsWithPatch() throws Exception {
        // Initialize the database
        medicalAppointmentsRepository.saveAndFlush(medicalAppointments);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medicalAppointments using partial update
        MedicalAppointments partialUpdatedMedicalAppointments = new MedicalAppointments();
        partialUpdatedMedicalAppointments.setId(medicalAppointments.getId());

        partialUpdatedMedicalAppointments.dateMedical(UPDATED_DATE_MEDICAL);

        restMedicalAppointmentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedicalAppointments.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMedicalAppointments))
            )
            .andExpect(status().isOk());

        // Validate the MedicalAppointments in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMedicalAppointmentsUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMedicalAppointments, medicalAppointments),
            getPersistedMedicalAppointments(medicalAppointments)
        );
    }

    @Test
    @Transactional
    void fullUpdateMedicalAppointmentsWithPatch() throws Exception {
        // Initialize the database
        medicalAppointmentsRepository.saveAndFlush(medicalAppointments);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medicalAppointments using partial update
        MedicalAppointments partialUpdatedMedicalAppointments = new MedicalAppointments();
        partialUpdatedMedicalAppointments.setId(medicalAppointments.getId());

        partialUpdatedMedicalAppointments.dateMedical(UPDATED_DATE_MEDICAL);

        restMedicalAppointmentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedicalAppointments.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMedicalAppointments))
            )
            .andExpect(status().isOk());

        // Validate the MedicalAppointments in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMedicalAppointmentsUpdatableFieldsEquals(
            partialUpdatedMedicalAppointments,
            getPersistedMedicalAppointments(partialUpdatedMedicalAppointments)
        );
    }

    @Test
    @Transactional
    void patchNonExistingMedicalAppointments() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalAppointments.setId(longCount.incrementAndGet());

        // Create the MedicalAppointments
        MedicalAppointmentsDTO medicalAppointmentsDTO = medicalAppointmentsMapper.toDto(medicalAppointments);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicalAppointmentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, medicalAppointmentsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(medicalAppointmentsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MedicalAppointments in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMedicalAppointments() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalAppointments.setId(longCount.incrementAndGet());

        // Create the MedicalAppointments
        MedicalAppointmentsDTO medicalAppointmentsDTO = medicalAppointmentsMapper.toDto(medicalAppointments);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicalAppointmentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(medicalAppointmentsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MedicalAppointments in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMedicalAppointments() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicalAppointments.setId(longCount.incrementAndGet());

        // Create the MedicalAppointments
        MedicalAppointmentsDTO medicalAppointmentsDTO = medicalAppointmentsMapper.toDto(medicalAppointments);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicalAppointmentsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(medicalAppointmentsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MedicalAppointments in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMedicalAppointments() throws Exception {
        // Initialize the database
        medicalAppointmentsRepository.saveAndFlush(medicalAppointments);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the medicalAppointments
        restMedicalAppointmentsMockMvc
            .perform(delete(ENTITY_API_URL_ID, medicalAppointments.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return medicalAppointmentsRepository.count();
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

    protected MedicalAppointments getPersistedMedicalAppointments(MedicalAppointments medicalAppointments) {
        return medicalAppointmentsRepository.findById(medicalAppointments.getId()).orElseThrow();
    }

    protected void assertPersistedMedicalAppointmentsToMatchAllProperties(MedicalAppointments expectedMedicalAppointments) {
        assertMedicalAppointmentsAllPropertiesEquals(
            expectedMedicalAppointments,
            getPersistedMedicalAppointments(expectedMedicalAppointments)
        );
    }

    protected void assertPersistedMedicalAppointmentsToMatchUpdatableProperties(MedicalAppointments expectedMedicalAppointments) {
        assertMedicalAppointmentsAllUpdatablePropertiesEquals(
            expectedMedicalAppointments,
            getPersistedMedicalAppointments(expectedMedicalAppointments)
        );
    }
}
